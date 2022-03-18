package com.onlinestore.onlinestore.exception;

public class ProductAlreadyInBasketException extends RuntimeException {
    public ProductAlreadyInBasketException(String message) {
        super(message);
    }
}
