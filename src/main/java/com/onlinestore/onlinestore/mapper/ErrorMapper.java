package com.onlinestore.onlinestore.mapper;

import com.onlinestore.onlinestore.exception.data.ApiError;
import org.springframework.http.ResponseEntity;

public class ErrorMapper {

    public static ResponseEntity errorToEntity(ApiError apiError) {
        return new ResponseEntity(apiError, apiError.getError());
    }
}
