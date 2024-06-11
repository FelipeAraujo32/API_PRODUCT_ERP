package com.product.product_api.service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.product.product_api.entity.Product;
import com.product.product_api.repository.ProductRepository;
import com.product.product_api.service.business_exception.BadRequestException;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;


@Service
public class ProductService {

    private final Validator validator;

    public ProductService() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        this.validator = factory.getValidator();
    }

    @Autowired
    private ProductRepository repository;

    public List<Product> findAllProduct(){
        return repository.findAll();
    }

    public Optional<Product> findProduct(UUID uuid){
        return repository.findById(uuid);
    }

    public Product createProduct(Product product){

        validationDataProduct(product);
        return repository.save(product);
    }

    public Product updateProduct(UUID uuid, Product product){
        Optional<Product> opt = repository.findById(uuid);
        if(opt.isEmpty()){
            throw new RuntimeException("Product not found");
        }
        validationDataProduct(product);
        return repository.save(product);
    }

    public void deleteProduct(UUID uuid){
        repository.deleteById(uuid);
    }

    //Considere criar uma abstração para essa classe
    // Method for validating data from the controller
    public void validationDataProduct(Product product){
        Set<ConstraintViolation<Product>> violations = validator.validate(product);
        if(!violations.isEmpty() || violations == null){
            StringBuilder sb = new StringBuilder();
            for (ConstraintViolation<Product> violation : violations){
                sb.append(violation.getMessage()).append(" \n ");
            }
            throw new BadRequestException(sb.toString());
        }
    }
    
}
