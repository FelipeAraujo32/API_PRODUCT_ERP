package com.product.product_api.service;

import java.util.Set;

import org.springframework.stereotype.Component;

import com.product.product_api.entity.Product;
import com.product.product_api.service.business_exception.BusinessException;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

@Component
public class ValidationDataProduct {

    private final Validator validator;

    public ValidationDataProduct() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        this.validator = factory.getValidator();
    }
    
    // Method for validating data from the controller
    public void validation(Product product) throws BusinessException{
        Set<ConstraintViolation<Product>> stringValidations = validator.validate(product);
        
        if(!stringValidations.isEmpty()){
            StringBuilder sb = new StringBuilder();
            for (ConstraintViolation<Product> validations : stringValidations){
                sb.append(validations.getMessage()).append(" \n");
            }
            throw new BusinessException(sb.toString());
        }
    }
}
