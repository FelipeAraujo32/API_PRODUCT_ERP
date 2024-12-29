package com.product.product_api.convert.product;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.product.product_api.dto.response.ResponseProductModelDto;
import com.product.product_api.entity.ProductModel;

@Component
public class ResponseConvertProduct {

    public ResponseProductModelDto convertToList(ProductModel productModel) {
        ResponseProductModelDto productModelDTO = new ResponseProductModelDto();
        productModelDTO.setProductId(productModel.getProductId());
        productModelDTO.setName(productModel.getName());
        productModelDTO.setDescription(productModel.getDescription());
        productModelDTO.setPrice(productModel.getPrice());
        return productModelDTO;
    }
    
    public List<ResponseProductModelDto> toProductDTOList(List<ProductModel> products) {
        return products.stream()
                .map(this::convertToList)
                .collect(Collectors.toList());
    }
}
