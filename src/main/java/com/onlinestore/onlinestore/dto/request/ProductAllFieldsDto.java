package com.onlinestore.onlinestore.dto.request;

import java.math.BigDecimal;

public class ProductAllFieldsDto {
    private Long id;
    private String name;
    private String description;
    private BigDecimal price;

    public ProductAllFieldsDto(Long id, String name, String description, BigDecimal price) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
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
