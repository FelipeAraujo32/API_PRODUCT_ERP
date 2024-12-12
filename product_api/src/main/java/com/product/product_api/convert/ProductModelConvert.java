package com.product.product_api.convert;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.product.product_api.dto.ProductModelDto;
import com.product.product_api.entity.ProductModel;

@Component
public class ProductModelConvert {

    public ProductModelDto toProductDTO(ProductModel productModel, ProductModelDto productModelDto) {
        ProductModelDto productModelDTO = new ProductModelDto();
        productModelDTO.setProductId(productModel.getProductId());
        productModelDTO.setName(productModel.getName());
        productModelDTO.setDescription(productModel.getDescription());
        productModelDTO.setPrice(productModel.getPrice());
        productModelDTO.setStock(productModelDto.getStock());
        return productModelDTO;
    }

    public ProductModel toProductModel(ProductModelDto productModelDTO) {
        ProductModel productModel = new ProductModel();
        productModel.setName(productModelDTO.getName());
        productModel.setDescription(productModelDTO.getDescription());
        productModel.setPrice(productModelDTO.getPrice());
        return productModel;
    }

    public ProductModelDto convertToList(ProductModel productModel) {
        ProductModelDto productModelDTO = new ProductModelDto();
        productModelDTO.setProductId(productModel.getProductId());
        productModelDTO.setName(productModel.getName());
        productModelDTO.setDescription(productModel.getDescription());
        productModelDTO.setPrice(productModel.getPrice());
        return productModelDTO;
    }

    public List<ProductModelDto> toProductDTOList(List<ProductModel> products) {
        return products.stream()
                .map(this::convertToList)
                .collect(Collectors.toList());
    }
}
