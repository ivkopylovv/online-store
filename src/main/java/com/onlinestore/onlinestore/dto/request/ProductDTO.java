package com.onlinestore.onlinestore.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProductDTO {
    private Long id;
    private String title;
    private String mainImage;
    private Double price;
}
