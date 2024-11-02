package com.nimbleways.springboilerplate.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ProductNotFoundException extends Exception{
    public ProductNotFoundException(String message) {
        super(message);
    }

}
