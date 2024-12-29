package com.product.product_api.convert.event;

import org.springframework.stereotype.Component;

import com.product.product_api.dto.event.ProductEventDto;
import com.product.product_api.dto.request.RequestProductModelDto;
import com.product.product_api.entity.ProductModel;

@Component
public class ProductEventConvert {
    
    public ProductEventDto toProductEventDTO(ProductModel productModel, RequestProductModelDto productModelDto) {
        ProductEventDto productEventDTO = new ProductEventDto();
        productEventDTO.setProductId(productModel.getProductId());
        productEventDTO.setStock(productModelDto.getStock());
        return productEventDTO;
    }
}
