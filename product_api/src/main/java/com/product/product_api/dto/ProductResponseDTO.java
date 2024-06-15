package com.product.product_api.dto;

import java.util.Optional;
import java.util.UUID;
import com.product.product_api.entity.Product;

public record ProductResponseDTO(
    UUID uuid,
    String name,
    String description,
    double price,
    Integer stock
) {
    
    public ProductResponseDTO (Optional<Product> productOptional){
        this(productOptional.orElse(null));
    }

    public ProductResponseDTO(Product product) {
        this(
            product.getUuid(),
            product.getName(),
            product.getDescription(),
            product.getPrice(),
            product.getStock()
        );
    }
}
