package com.product.product_api.dto;

import com.product.product_api.entity.Product;

public record ProductDTO(

    String name,
    String description,
    double price,
    Integer stock
) {

    public Product toProduct(){
        Product product = new Product();
        product.setName(name);
        product.setDescription(description);
        product.setPrice(price);
        product.setStock(stock);
        return product;
    } 

}
