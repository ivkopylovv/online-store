package com.onlinestore.onlinestore.dto.request;

import java.math.BigDecimal;

public class ProductAllFieldsDto {
    private Long id;
    private String name;
    private String image;
    private String description;
    private BigDecimal price;

    public ProductAllFieldsDto(Long id, String name, String description, String image, BigDecimal price) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.description = description;
        this.price = price;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public BigDecimal getPrice() {
        return price;
    }


}
