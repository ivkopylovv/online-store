package com.onlinestore.onlinestore.dto.response;

public class CountDto {
    private Long count;

    public long getCount() {
        return count;
    }

    public CountDto(long count) {
        this.count = count;
    }

    public void setCount(long count) {
        this.count = count;
    }
}
