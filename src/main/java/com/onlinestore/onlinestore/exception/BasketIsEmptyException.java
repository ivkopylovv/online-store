package com.onlinestore.onlinestore.exception;

public class BasketIsEmptyException extends RuntimeException {
    public BasketIsEmptyException(String message) {
        super(message);
    }
}
