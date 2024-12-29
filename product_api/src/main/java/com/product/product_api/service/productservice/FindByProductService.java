package com.product.product_api.service.productservice;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.product.product_api.convert.product.ResponseConvertProduct;
import com.product.product_api.dto.response.ResponseProductModelDto;
import com.product.product_api.entity.ProductModel;
import com.product.product_api.repository.ProductRepository;
import com.product.product_api.service.business_exception.NotFoundException;

@Service
public class FindByProductService {

    private final ProductRepository productRepository;
    private final ResponseConvertProduct responseConvertProduct;

    public FindByProductService(ProductRepository productRepository, ResponseConvertProduct responseConvertProduct) {
        this.productRepository = productRepository;
        this.responseConvertProduct = responseConvertProduct;
    }

    public List<ResponseProductModelDto> findAllProduct() throws NotFoundException {
        List<ProductModel> allProducts = productRepository.findAll();
        if (allProducts.isEmpty()) {
            throw new NotFoundException("Products list not found");
        }
        return responseConvertProduct.toProductDTOList(allProducts);
    }

    public ResponseProductModelDto findByProduct(UUID uuid) {
        ProductModel findByProduct = productRepository.findById(uuid).orElseThrow(NoSuchElementException::new);
        return responseConvertProduct.convertToList(findByProduct);
    }

    
}
