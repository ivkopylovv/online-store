package com.onlinestore.onlinestore.exception;

public class ProductAlreadyInFavouritesException extends RuntimeException {
    public ProductAlreadyInFavouritesException(String message) {
        super(message);
    }
}
