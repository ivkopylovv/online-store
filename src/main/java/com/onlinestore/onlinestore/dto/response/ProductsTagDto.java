package com.onlinestore.onlinestore.dto.response;

public class ProductsTagDto {
    private String type;
    private String value;

    public ProductsTagDto(String type, String value) {
        this.type = type;
        this.value = value;
    }

    public ProductsTagDto() {
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
