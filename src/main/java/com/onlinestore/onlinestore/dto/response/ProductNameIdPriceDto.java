package com.onlinestore.onlinestore.dto.response;

import java.math.BigDecimal;

public class ProductNameIdPriceDto {
    private Long id;
    private String name;
    private BigDecimal price;

    public ProductNameIdPriceDto(Long id, String name, BigDecimal price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public ProductNameIdPriceDto() {
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }
}
