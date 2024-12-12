package com.product.product_api.dto;

import java.math.BigDecimal;
import java.util.UUID;

import jakarta.validation.constraints.PositiveOrZero;

public class ProductModelDto {

    private UUID productId;
    private String name;
    private String description;
    private BigDecimal price;
    @PositiveOrZero(message = "Stock cannot be negative")
    private Integer stock;
    
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public BigDecimal getPrice() {
        return price;
    }
    public void setPrice(BigDecimal price) {
        this.price = price;
    }
    public Integer getStock() {
        return stock;
    }
    public void setStock(Integer stock) {
        this.stock = stock;
    }
    public UUID getProductId() {
        return productId;
    }
    public void setProductId(UUID productId) {
        this.productId = productId;
    }


}
