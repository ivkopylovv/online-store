package com.onlinestore.onlinestore.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ProductAllFieldsDto {
    private Long id;
    private String name;
    private String image;
    private String description;
    private Double price;
    
}
