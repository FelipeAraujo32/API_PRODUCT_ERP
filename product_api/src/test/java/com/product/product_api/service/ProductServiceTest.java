package com.product.product_api.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

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

public class ProductServiceTest {

    @InjectMocks
    private ProductService service;

    @Mock
    private ProductRepository repository;

    @Mock
    private ValidationDataService validationService;

    Product product = new Product("Felipe", "Araujo", 10, 50);

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
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
    @DisplayName("Must return a business exception if validation returns incorrectl")
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
    void testDeleteProduct() {

    }

    @Test
    void testFindAllProduct() {

    }

    @Test
    void testFindById() {

    }

    @Test
    void testUpdateProduct() {

    }
}
