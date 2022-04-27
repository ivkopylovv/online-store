package com.onlinestore.onlinestore.exception;

public class ProductAlreadyInCartException extends RuntimeException {
    public ProductAlreadyInCartException(String message) {
        super(message);
    }
}
