package com.product.product_api.service.business_exception;

public class NotFoundException extends BusinessException{
    
    public NotFoundException(String message){
        super(message);
    }

}
