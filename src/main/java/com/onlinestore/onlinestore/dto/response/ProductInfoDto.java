package com.onlinestore.onlinestore.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ProductInfoDto {
    private Long id;
    private String name;
    private BigDecimal price;
    private String image;

}
