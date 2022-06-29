package com.onlinestore.onlinestore.dto.response;

import com.onlinestore.onlinestore.dto.request.ProductDTO;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ProductListDTO {
    private List<ProductDTO> products;
}
