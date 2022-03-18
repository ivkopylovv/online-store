package com.onlinestore.onlinestore.dto.response;

public class ProductIdDto {
    Long productId;

    public ProductIdDto() {
    }

    public ProductIdDto(Long productId) {
        this.productId = productId;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }
}
