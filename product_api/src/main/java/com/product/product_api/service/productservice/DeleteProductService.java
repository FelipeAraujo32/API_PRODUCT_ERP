package com.product.product_api.service.productservice;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.product.product_api.messaging.producer.InventoryProducer;
import com.product.product_api.repository.ProductRepository;

@Service
public class DeleteProductService {
    
    private final ProductRepository productRepository;
    private final InventoryProducer inventoryProducer;

    public DeleteProductService(ProductRepository productRepository, InventoryProducer inventoryProducer) {
        this.productRepository = productRepository;
        this.inventoryProducer = inventoryProducer;
    }

    public void deleteProduct(UUID uuid) {
        productRepository.deleteById(uuid);
        inventoryProducer.deleteProduct(uuid);
    }
}
