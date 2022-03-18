package com.onlinestore.onlinestore.exception;

public class BasketIsEmpty extends RuntimeException {
    public BasketIsEmpty(String message) {
        super(message);
    }
}
