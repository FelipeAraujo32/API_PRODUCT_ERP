package com.product.product_api.controller.exception;

import java.util.NoSuchElementException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.product.product_api.service.business_exception.BusinessException;
import com.product.product_api.service.business_exception.NotFoundException;

@RestControllerAdvice
public class GlobalException {

    private final Logger logger = LoggerFactory.getLogger(GlobalException.class);

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<String> handleBussinessException(BusinessException ex){
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(ex.getMessage());
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<String> handleNotFoundException(NotFoundException notFoundException){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(notFoundException.getMessage());
    }

    @ExceptionHandler
    public ResponseEntity<String> handleNotFoundElement(NoSuchElementException notFoundException) {
        return new ResponseEntity<>("Resource UUID not found.", HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<String> handleUnexpectedException(Throwable unexpectedException){
        var message = "Unexpected server error, see the logs.";
        logger.error(message, unexpectedException);
        return new ResponseEntity<>(message, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
