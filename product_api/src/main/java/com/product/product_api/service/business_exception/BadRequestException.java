package com.product.product_api.service.business_exception;

public class BadRequestException extends RuntimeException{

    public BadRequestException(String message){
        super(message);
    }
    
}
