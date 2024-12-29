package com.product.product_api.dto.event;

import java.util.UUID;

public class ProductEventDto {
    
    private UUID productId;
    private int Stock;
    
    public UUID getProductId() {
        return productId;
    }
    public void setProductId(UUID productId) {
        this.productId = productId;
    }
    public int getStock() {
        return Stock;
    }
    public void setStock(int stock) {
        Stock = stock;
    }
    
    

    
}
