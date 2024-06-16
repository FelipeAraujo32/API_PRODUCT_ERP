package com.product.product_api.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.product.product_api.entity.Product;
import com.product.product_api.repository.ProductRepository;
import com.product.product_api.service.business_exception.BusinessException;
import com.product.product_api.service.business_exception.NotFoundException;

public class ProductServiceTest {

    @InjectMocks
    private ProductService service;

    @Mock
    private ProductRepository repository;

    @Mock
    private ValidationDataService validationService;

    Product product;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        product = new Product("Felipe", "Araujo", 10, 50);
        product.setUuid(UUID.randomUUID());
    }

    @Test
    @DisplayName("The product should save successfully when everything is OK")
    void testCreateProductCase1() {

        doNothing().when(validationService).validation(product);

        when(repository.save(product)).thenReturn(product);

        Product result = service.createProduct(product);

        verify(validationService).validation(product);

        verify(repository, times(1)).save(product);

        assertNotNull(result);
        assertEquals(product, result);
    }

    @Test
    @DisplayName("Must return a business exception if validation returns incorrect")
    void testCreateProductCase2() {

        doThrow(new BusinessException(toString())).when(validationService).validation(product);

        BusinessException exception = Assertions.assertThrows(BusinessException.class,
                () -> service.createProduct(product));

        assertEquals(toString(), exception.getMessage());

        verify(validationService, times(1)).validation(product);
        verifyNoInteractions(repository);
    }

    @Test
    @DisplayName("Validation Service behavior if there is a Repository Exception")
    void testCreateProductCase3() {

        doNothing().when(validationService).validation(product);

        when(repository.save(product)).thenThrow(new RuntimeException("Database connection failed"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            service.createProduct(product);
        });
        assertEquals("Database connection failed", exception.getMessage());

        verify(validationService, times(1)).validation(product);
        verify(repository, times(1)).save(product);
    }

    @Test
    @DisplayName("Success case when deleting the product")
    void testDeleteProductCase1() {

        when(repository.findById(product.getUuid())).thenReturn(Optional.of(product));

        service.deleteProduct(product.getUuid());

        verify(repository, times(1)).findById(product.getUuid());
        verify(repository, times(1)).delete(product);
    }

    @Test
    @DisplayName("Product not found case")
    void testDeleteProductCase2() {

        when(repository.findById(product.getUuid())).thenReturn(Optional.empty());

        NoSuchElementException exception = Assertions.assertThrows(NoSuchElementException.class,
                () -> service.deleteProduct(product.getUuid()));

        Assertions.assertEquals(exception, exception);

        verify(repository, times(1)).findById(product.getUuid());
    }

    @Test
    @DisplayName("Case confirm Deletion Product")
    void testDeleteProductCase3() {

        Optional<Product> productBefore = repository.findById(product.getUuid());
        assertTrue(productBefore.isEmpty());

        repository.delete(product);

        Optional<Product> productAfter = repository.findById(product.getUuid());
        assertFalse(productAfter.isPresent());
    }

    @Test
    @DisplayName("Successful case returns all products")
    void testFindAllProductCase1() {

        when(repository.findAll()).thenReturn(List.of(product));

        service.findAllProduct();

        verify(repository).findAll();
    }

    @Test
    @DisplayName("Case of returning an empty product list")
    void testFindAllProductCase2() {
        when(repository.findAll()).thenReturn(Collections.emptyList());

        NotFoundException exception = Assertions.assertThrows(NotFoundException.class, () -> service.findAllProduct());

        assertEquals("Products list not found", exception.getMessage());

        verify(repository, times(1)).findAll();
    }

    @Test
    void testFindById() {

    }

    @Test
    void testUpdateProduct() {

    }
}
