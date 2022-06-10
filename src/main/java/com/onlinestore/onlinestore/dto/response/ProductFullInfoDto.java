package com.onlinestore.onlinestore.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ProductFullInfoDto {
    private Long id;
    private String name;
    private String description;
    private Double price;
    private List<String> images;
    private List<ProductsTagDto> tags;

}
