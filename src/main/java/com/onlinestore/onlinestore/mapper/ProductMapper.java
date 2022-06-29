package com.onlinestore.onlinestore.mapper;

import com.onlinestore.onlinestore.dto.request.ProductDTO;
import com.onlinestore.onlinestore.entity.Product;

import java.util.List;
import java.util.stream.Collectors;

public class ProductMapper {

    public static List<ProductDTO> entityListToDTOList(List<Product> products) {

        return products.stream()
                .map(p -> new ProductDTO(
                        p.getId(),
                        p.getTitle(),
                        p.getMainImage(),
                        p.getPrice()))
                .collect(Collectors.toList());
    }
}
