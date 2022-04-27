package com.onlinestore.onlinestore.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class FullProductDto {
    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private List<String> images;
    private List<ProductsTagDto> tags;

}
