package com.product.product_api.convert;

import org.springframework.stereotype.Component;

import com.product.product_api.dto.ProductEventDto;
import com.product.product_api.dto.ProductModelDto;
import com.product.product_api.entity.ProductModel;

@Component
public class ProductEventConvert {
    
    public ProductEventDto toProductEventDTO(ProductModel productModel, ProductModelDto productModelDto) {
        ProductEventDto productEventDTO = new ProductEventDto();
        productEventDTO.setProductId(productModel.getProductId());
        productEventDTO.setStock(productModelDto.getStock());
        return productEventDTO;
    }
}
