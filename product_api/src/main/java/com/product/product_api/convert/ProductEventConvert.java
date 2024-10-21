package com.product.product_api.convert;

import org.springframework.stereotype.Component;

import com.product.product_api.dto.ProductEventDto;
import com.product.product_api.entity.ProductModel;

@Component
public class ProductEventConvert {
    
    public ProductEventDto toProductEventDTO(ProductModel productModel) {
        ProductEventDto productEventDTO = new ProductEventDto();
        productEventDTO.setProductId(productModel.getProductId());
        productEventDTO.setStock(productModel.getStock());
        return productEventDTO;
    }
}
