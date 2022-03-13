package com.onlinestore.onlinestore.dto.response;

public class SuccessMessageDto {
    private String message;

    public SuccessMessageDto(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
