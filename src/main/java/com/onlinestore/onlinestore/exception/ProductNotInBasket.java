package com.onlinestore.onlinestore.exception;

public class ProductNotInBasket extends RuntimeException {
    public ProductNotInBasket(String message) {
        super(message);
    }
}
