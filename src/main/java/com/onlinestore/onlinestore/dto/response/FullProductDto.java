package com.onlinestore.onlinestore.dto.response;

import java.math.BigDecimal;
import java.util.ArrayList;

public class FullProductDto {
    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private ArrayList<String> images;
    private ArrayList<ProductsTagDto> tags;

    public FullProductDto() {
    }

    public FullProductDto(Long id, String name, String description, BigDecimal price, ArrayList<String> images, ArrayList<ProductsTagDto> tags) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.images = images;
        this.tags = tags;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public ArrayList<String> getImages() {
        return images;
    }

    public void setImages(ArrayList<String> images) {
        this.images = images;
    }

    public ArrayList<ProductsTagDto> getTags() {
        return tags;
    }

    public void setTags(ArrayList<ProductsTagDto> tags) {
        this.tags = tags;
    }
}
