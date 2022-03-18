package com.onlinestore.onlinestore.dto.request;

public class UserBasketClearDto {
    private Long userId;

    public UserBasketClearDto() {
    }

    public UserBasketClearDto(Long userId) {
        this.userId = userId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserIdId(Long userId) {
        this.userId = userId;
    }
}
