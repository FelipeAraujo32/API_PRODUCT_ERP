package com.product.product_api.service.productservice;

import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.product.product_api.convert.ProductModelConvert;
import com.product.product_api.dto.ProductModelDto;
import com.product.product_api.entity.ProductModel;
import com.product.product_api.messaging.producer.InventoryProducer;
import com.product.product_api.repository.ProductRepository;
import com.product.product_api.service.ValidationDataService;
import com.product.product_api.service.business_exception.NotFoundException;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class ProductServiceUpdate {

    private final ValidationDataService validationDataService;
    private final ProductRepository productRepository;
    private final InventoryProducer inventoryProducer;
    private final ProductModelConvert productModelConvert;

    public ProductServiceUpdate(ValidationDataService validationDataService, ProductRepository productRepository,
            InventoryProducer inventoryProducer, ProductModelConvert productModelConvert) {
        this.validationDataService = validationDataService;
        this.productRepository = productRepository;
        this.inventoryProducer = inventoryProducer;
        this.productModelConvert = productModelConvert;
    }
    
    public ProductModelDto updateProductAndSyncInventory(UUID productId, ProductModelDto productDTO) {
        ProductModel productModel = productModelConvert.toProductModel(productDTO);
        ProductModel byProductId = findExistingProduct(productId);
        updateProductFields(byProductId, productDTO);
        validationDataService.validation(productModel);
        ProductModel updateProduct = productRepository.save(productModel);
        notifyUpdateInventoryService(updateProduct, productDTO);
        return productModelConvert.toProductDTO(updateProduct, productDTO);
    }

    private void notifyUpdateInventoryService(ProductModel product, ProductModelDto productDTO) {
        try {
            inventoryProducer.updateProduct(product, productDTO);
        } catch (Exception ex) {
            throw new IllegalStateException("Failed to notify inventory service for product ID: " + product.getProductId(), ex);
        }
    }

    private ProductModel findExistingProduct(UUID productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException("Product not found for ID: " + productId));
    }

    private void updateProductFields(ProductModel product, ProductModelDto productDTO) {
        Optional.ofNullable(productDTO.getName()).ifPresent(product::setName);
        Optional.ofNullable(productDTO.getDescription()).ifPresent(product::setDescription);
        Optional.ofNullable(productDTO.getPrice()).ifPresent(product::setPrice);
    }
}
