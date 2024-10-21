package com.product.product_api.convert;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.product.product_api.dto.ProductModelDto;
import com.product.product_api.entity.ProductModel;

@Component
public class ProductModelConvert {

    public ProductModelDto toProductDTO(ProductModel productModel) {
        ProductModelDto productModelDTO = new ProductModelDto();
        productModelDTO.setProductId(productModel.getProductId());
        productModelDTO.setName(productModel.getName());
        productModelDTO.setDescription(productModel.getDescription());
        productModelDTO.setPrice(productModel.getPrice());
        productModelDTO.setStock(productModel.getStock());
        return productModelDTO;
    }

    public ProductModel toProductModel(ProductModelDto productModelDTO) {
        ProductModel productModel = new ProductModel();
        productModel.setName(productModelDTO.getName());
        productModel.setDescription(productModelDTO.getDescription());
        productModel.setPrice(productModelDTO.getPrice());
        productModel.setStock(productModelDTO.getStock());
        return productModel;
    }

    public List<ProductModelDto> toProductDTOList(List<ProductModel> products) {
        return products.stream()
                .map(this::toProductDTO)
                .collect(Collectors.toList());
    }
}
