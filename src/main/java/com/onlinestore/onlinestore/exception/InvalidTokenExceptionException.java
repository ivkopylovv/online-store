package com.onlinestore.onlinestore.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class InvalidTokenExceptionException extends RuntimeException {
    public InvalidTokenExceptionException(String message) {
        super(message);
    }
}
