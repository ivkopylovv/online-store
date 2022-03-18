package com.onlinestore.onlinestore.dto.request;

public class UserIdDto {
    Long userId;

    public UserIdDto() {
    }

    public UserIdDto(Long userId) {
        this.userId = userId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
