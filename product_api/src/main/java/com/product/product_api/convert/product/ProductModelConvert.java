package com.product.product_api.convert.product;

import org.springframework.stereotype.Component;

import com.product.product_api.dto.request.RequestProductModelDto;
import com.product.product_api.entity.ProductModel;

@Component
public class ProductModelConvert {

    public RequestProductModelDto toProductDTO(ProductModel productModel, RequestProductModelDto productModelDto) {
        RequestProductModelDto productModelDTO = new RequestProductModelDto();
        productModelDTO.setProductId(productModel.getProductId());
        productModelDTO.setName(productModel.getName());
        productModelDTO.setDescription(productModel.getDescription());
        productModelDTO.setPrice(productModel.getPrice());
        productModelDTO.setStock(productModelDto.getStock());
        return productModelDTO;
    }

    public ProductModel toProductModel(RequestProductModelDto productModelDTO) {
        ProductModel productModel = new ProductModel();
        productModel.setName(productModelDTO.getName());
        productModel.setDescription(productModelDTO.getDescription());
        productModel.setPrice(productModelDTO.getPrice());
        return productModel;
    }

    

    
}
