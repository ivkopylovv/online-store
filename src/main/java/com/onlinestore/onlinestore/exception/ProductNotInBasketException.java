package com.onlinestore.onlinestore.exception;

public class ProductNotInBasketException extends RuntimeException {
    public ProductNotInBasketException(String message) {
        super(message);
    }
}
