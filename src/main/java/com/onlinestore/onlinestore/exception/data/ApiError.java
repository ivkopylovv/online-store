package com.onlinestore.onlinestore.exception.data;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.util.Date;

import static com.fasterxml.jackson.annotation.JsonFormat.Shape.STRING;

@Data
@AllArgsConstructor
public class ApiError {

    @JsonFormat(shape = STRING)
    private Date timestamp;
    private Integer status;
    private HttpStatus error;
    private String message;
    private String path;
}
