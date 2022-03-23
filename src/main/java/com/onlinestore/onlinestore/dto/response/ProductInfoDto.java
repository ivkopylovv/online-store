package com.onlinestore.onlinestore.dto.response;

import java.math.BigDecimal;

public class ProductInfoDto {
    private Long id;
    private String name;
    private BigDecimal price;
    private String image;

    public ProductInfoDto(Long id, String name, BigDecimal price, String image) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.image = image;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
