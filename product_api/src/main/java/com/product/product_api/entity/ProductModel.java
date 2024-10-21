package com.product.product_api.entity;

import java.math.BigDecimal;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

@Entity(name = "product")
@Table(name = "product_erp")
public class ProductModel {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID productId;

    @Column(name = "name_prodocut" ,nullable = false)
    @NotNull(message = "Name cannot be Null")
    @NotEmpty(message = "Name cannot be empty")
    private String name;

    @Column(nullable = false)
    @NotNull(message = "Description cannot be Null")
    @NotEmpty(message = "Description cannot be empty")
    private String description;

    @Column(nullable = false)
    @NotNull(message = "Price cannot be Null")
    @Positive(message = "The price cannot be zero")
    private BigDecimal price;
    
    @Column(nullable = false)
    @PositiveOrZero(message = "Stock cannot be negative")
    private Integer stock;
    
    public ProductModel() {
    }

    public ProductModel(String name, String description, BigDecimal price, int stock) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.stock = stock;
    }

    public ProductModel(String name, String description, double price, int stock) {
        this(name, description, BigDecimal.valueOf(price), stock);
    }
    
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

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public UUID getProductId() {
        return productId;
    }

    public void setProductId(UUID productId) {
        this.productId = productId;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }
}