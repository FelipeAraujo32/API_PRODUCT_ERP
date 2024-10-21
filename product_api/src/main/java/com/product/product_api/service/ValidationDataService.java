package com.product.product_api.service;

import java.util.Set;

import org.springframework.stereotype.Component;

import com.product.product_api.entity.ProductModel;
import com.product.product_api.service.business_exception.BusinessException;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

@Component
public class ValidationDataService {

    private final Validator validator;

    public ValidationDataService() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        this.validator = factory.getValidator();
    }
    
    public void validation(ProductModel product) throws BusinessException{
        Set<ConstraintViolation<ProductModel>> stringValidations = validator.validate(product);
        
        if(!stringValidations.isEmpty()){
            StringBuilder sb = new StringBuilder();
            for (ConstraintViolation<ProductModel> validations : stringValidations){
                sb.append(validations.getMessage()).append(" \n");
            }
            throw new BusinessException(sb.toString());
        }
    }
}
