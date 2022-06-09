package com.onlinestore.onlinestore.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class ProductNotInFavouritesException extends RuntimeException {
    public ProductNotInFavouritesException(String message) {
        super(message);
    }

}
