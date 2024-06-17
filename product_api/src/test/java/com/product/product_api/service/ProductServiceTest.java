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

        assertThrows(NoSuchElementException.class,
                () -> service.deleteProduct(product.getUuid()));

        verify(repository, times(1)).findById(product.getUuid());
        verify(repository, times(0)).delete(product);
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

        List<Product> result = service.findAllProduct();

        assertNotNull(result);
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
    @DisplayName("If successful, returns the specific product passed by uuid")
    void testFindByIdCase1() {

        when(repository.findById(product.getUuid())).thenReturn(Optional.of(product));

        Product result = service.findById(product.getUuid());

        assertNotNull(result);
        assertEquals(product, result);

        verify(repository, times(1)).findById(product.getUuid());
    }

    @Test
    @DisplayName("Exception case if uuid is not found")
    void testFindByIdCase2() {

        when(repository.findById(product.getUuid())).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class,
                () -> service.findById(product.getUuid()));

        verify(repository, times(1)).findById(product.getUuid());
    }

    @Test
    @DisplayName("Success case saves the non-bank product if everything is ok")
    void testUpdateProductCase1() {

        when(repository.findById(product.getUuid())).thenReturn(Optional.of(product));

        doNothing().when(validationService).validation(product);

        when(repository.save(product)).thenReturn(product);

        Product result = service.updateProduct(product.getUuid(), product);

        verify(repository, times(1)).findById(product.getUuid());
        verify(validationService, times(1)).validation(product);
        verify(repository, times(1)).save(product);

        assertNotNull(result);
        assertEquals(product, result);
    }

    @Test
    @DisplayName("If the product UUID does not exist, an exception is returned")
    void testUpdateProductCase2() {

        when(repository.findById(product.getUuid())).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class,
                () -> service.updateProduct(product.getUuid(), product));

        verify(repository, times(1)).findById(product.getUuid());
        verify(validationService, times(1)).validation(product);
        verify(repository, times(0)).save(product);

    }

    @Test
    @DisplayName("If the UUID returning from the bank is different from the UUID passed by the customer, an exception is returned")
    void testUpdateProductCase3() {

        Product product_1 = new Product("Heloisa", "Araujo", 90, 60);
        product_1.setUuid(UUID.randomUUID());

        when(repository.findById(product.getUuid())).thenReturn(Optional.of(product_1));

        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> service.updateProduct(product.getUuid(), product));

        assertEquals("The UUID must be the same as the one you want to update", exception.getMessage());

        verify(repository, times(1)).findById(product.getUuid());
        verify(repository, times(0)).save(product);

    }

    @Test
    @DisplayName("Case of returning an exception if the data passed is incorrect")
    void testUpdateProductCase4() {

        when(repository.findById(product.getUuid())).thenReturn(Optional.of(product));

        doThrow(new BusinessException(toString())).when(validationService).validation(product);

        BusinessException exception = Assertions.assertThrows(BusinessException.class,
                () -> service.updateProduct(product.getUuid(), product));

        assertEquals(toString(), exception.getMessage());

        verify(repository, times(1)).findById(product.getUuid());
        verify(validationService, times(1)).validation(product);
        verify(repository, times(0)).delete(product);

    }

    @Test
    @DisplayName("Validation Service behavior if there is a Repository Exception")
    void testUpdateProductCase5() {

        when(repository.findById(product.getUuid())).thenReturn(Optional.of(product));
        doNothing().when(validationService).validation(product);

        when(repository.save(product)).thenThrow(new RuntimeException("Database connection failed"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            service.updateProduct(product.getUuid(), product);
        });
        assertEquals("Database connection failed", exception.getMessage());

        verify(validationService, times(1)).validation(product);
        verify(repository, times(1)).save(product);
    }
}
