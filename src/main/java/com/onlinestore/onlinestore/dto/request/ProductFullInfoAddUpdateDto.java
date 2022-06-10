package com.onlinestore.onlinestore.dto.request;

import com.onlinestore.onlinestore.dto.response.ProductsTagDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ProductFullInfoAddUpdateDto {
    private String name;
    private String description;
    private String image;
    private Double price;
    private List<String> images;
    private List<ProductsTagDto> tags;
}
