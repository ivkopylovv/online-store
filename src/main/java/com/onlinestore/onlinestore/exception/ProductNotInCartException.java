package com.onlinestore.onlinestore.exception;

public class ProductNotInCartException extends RuntimeException {
    public ProductNotInCartException(String message) {
        super(message);
    }
}
