package com.product.product_api.service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.product.product_api.convert.ProductModelConvert;
import com.product.product_api.dto.ProductModelDto;
import com.product.product_api.entity.ProductModel;
import com.product.product_api.messaging.producer.InventoryProducer;
import com.product.product_api.repository.ProductRepository;
import com.product.product_api.service.business_exception.NotFoundException;

@Service
public class ProductService {

    private final ValidationDataService validationDataService;
    private final ProductRepository productRepository;
    private final InventoryProducer inventoryProducer;
    private final ProductModelConvert productModelConvert;

    public ProductService(ValidationDataService validationDataService, ProductRepository productRepository,
            InventoryProducer inventoryProducer, ProductModelConvert productModelConvert) {
        this.validationDataService = validationDataService;
        this.productRepository = productRepository;
        this.inventoryProducer = inventoryProducer;
        this.productModelConvert = productModelConvert;
    }

    public List<ProductModelDto> findAllProduct() throws NotFoundException {
        List<ProductModel> allProducts = productRepository.findAll();
        if (allProducts.isEmpty()) {
            throw new NotFoundException("Products list not found");
        }
        return productModelConvert.toProductDTOList(allProducts);
    }

    public ProductModelDto findByProduct(UUID uuid) {
        ProductModel findByProduct = productRepository.findById(uuid).orElseThrow(NoSuchElementException::new);
        return productModelConvert.toProductDTO(findByProduct);
    }

    public ProductModelDto createProduct(ProductModelDto productDTO) {
        ProductModel productModel = productModelConvert.toProductModel(productDTO);
        validationDataService.validation(productModel);
        ProductModel createdProduct = productRepository.save(productModel);
        inventoryProducer.createProduct(createdProduct);
        return productModelConvert.toProductDTO(createdProduct);
    }

    public ProductModelDto updateProduct(UUID productId, ProductModelDto productDTO) {
        ProductModel productModel = productModelConvert.toProductModel(productDTO);
        ProductModel findByProductId = findByProductId(productId);
        checkCorrespondingUIDD(findByProductId, productId);
        validationDataService.validation(productModel);
        productModel.setProductId(productId);
        ProductModel updateProduct = productRepository.save(productModel);
        inventoryProducer.updateProduct(updateProduct);
        return productModelConvert.toProductDTO(updateProduct);
    }

    private ProductModel findByProductId(UUID productId) {
        return productRepository.findById(productId)
                .orElseThrow(
                        () -> new NotFoundException("Product with productId " + productId + " not found."));
    }

    private void checkCorrespondingUIDD(ProductModel findByProductId, UUID productId) {
        if (!findByProductId.getProductId().equals(productId)) {
            throw new NotFoundException("The UUID must be the same as the one you want to update");
        }
    }

    public void deleteProduct(UUID uuid) {
        productRepository.deleteById(uuid);
        inventoryProducer.deleteProduct(uuid);
    }
}
