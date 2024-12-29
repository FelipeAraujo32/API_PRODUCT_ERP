package com.product.product_api.service.productservice;

import org.springframework.stereotype.Service;

import com.product.product_api.convert.product.ProductModelConvert;
import com.product.product_api.dto.request.RequestProductModelDto;
import com.product.product_api.entity.ProductModel;
import com.product.product_api.messaging.producer.InventoryProducer;
import com.product.product_api.repository.ProductRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class CreatedProductService {

    private final ValidationDataService validationDataService;
    private final ProductRepository productRepository;
    private final InventoryProducer inventoryProducer;
    private final ProductModelConvert productModelConvert;

    public CreatedProductService(ValidationDataService validationDataService, ProductRepository productRepository,
            InventoryProducer inventoryProducer, ProductModelConvert productModelConvert) {
        this.validationDataService = validationDataService;
        this.productRepository = productRepository;
        this.inventoryProducer = inventoryProducer;
        this.productModelConvert = productModelConvert;
    }

    public RequestProductModelDto createProductAndNotifyInventory(RequestProductModelDto productDTO) {
        ProductModel productModel = productModelConvert.toProductModel(productDTO);
        validationDataService.validation(productModel);
        ProductModel createdProduct = productRepository.save(productModel);
        notifyCreateInventoryService(createdProduct, productDTO);
        return productModelConvert.toProductDTO(createdProduct, productDTO);
    }

    private void notifyCreateInventoryService(ProductModel product, RequestProductModelDto productDTO) {
        try {
            inventoryProducer.createProduct(product, productDTO);
        } catch (Exception ex) {
            throw new IllegalStateException("Failed to notify inventory service for product ID: " + product.getProductId(), ex);
        }
    }
}
