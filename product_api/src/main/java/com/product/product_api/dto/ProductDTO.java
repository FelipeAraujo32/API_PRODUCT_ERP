package com.product.product_api.dto;

import java.util.Optional;
import java.util.UUID;

import com.product.product_api.entity.Product;

public record ProductDTO(
    
    UUID uuid,
    String name,
    String description,
    double price,
    Integer stock
) {

    public ProductDTO(Product product){
        this(
            product.getUuid(),
            product.getName(),
            product.getDescription(),
            product.getPrice(),
            product.getStock()
        );
    }

    public ProductDTO (Optional<Product> productOptional){
        this(productOptional.orElse(null));
    }

    public Product toProduct(){
        Product product = new Product();
        product.setUuid(uuid);
        product.setName(name);
        product.setDescription(description);
        product.setPrice(price);
        product.setStock(stock);
        return product;
    }

}
