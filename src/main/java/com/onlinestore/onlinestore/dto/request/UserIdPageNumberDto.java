package com.onlinestore.onlinestore.dto.request;

public class UserIdPageNumberDto {
    Long userId;
    int pageNumber;

    public UserIdPageNumberDto() {
    }

    public UserIdPageNumberDto(Long userId, int pageNumber) {
        this.userId = userId;
        this.pageNumber = pageNumber;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }
}
