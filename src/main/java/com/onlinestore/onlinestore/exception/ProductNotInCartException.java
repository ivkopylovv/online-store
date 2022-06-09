package com.onlinestore.onlinestore.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class ProductNotInCartException extends RuntimeException {
    public ProductNotInCartException(String message) {
        super(message);
    }
}
