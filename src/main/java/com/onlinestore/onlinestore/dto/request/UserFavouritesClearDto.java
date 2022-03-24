package com.onlinestore.onlinestore.dto.request;

public class UserFavouritesClearDto {
    private Long userId;

    public UserFavouritesClearDto() {
    }

    public UserFavouritesClearDto(Long userId) {
        this.userId = userId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
