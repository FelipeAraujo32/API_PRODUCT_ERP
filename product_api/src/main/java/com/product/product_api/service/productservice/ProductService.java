package com.product.product_api.service.productservice;

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

    private final ProductRepository productRepository;
    private final InventoryProducer inventoryProducer;
    private final ProductModelConvert productModelConvert;

    public ProductService(ProductRepository productRepository,
            InventoryProducer inventoryProducer, ProductModelConvert productModelConvert) {
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
        return productModelConvert.convertToList(findByProduct);
    }

    public void deleteProduct(UUID uuid) {
        productRepository.deleteById(uuid);
        inventoryProducer.deleteProduct(uuid);
    }
}
