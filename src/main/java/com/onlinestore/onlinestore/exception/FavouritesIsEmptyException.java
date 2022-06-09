package com.onlinestore.onlinestore.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class FavouritesIsEmptyException extends RuntimeException {
    public FavouritesIsEmptyException(String message) {
        super(message);
    }
}
