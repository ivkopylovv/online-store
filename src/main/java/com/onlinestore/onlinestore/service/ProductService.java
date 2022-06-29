package com.onlinestore.onlinestore.service;

import com.onlinestore.onlinestore.dto.request.ProductDTO;
import com.onlinestore.onlinestore.entity.Product;

import java.util.List;

public interface ProductService {
    Product getProduct(Long id);

    List<ProductDTO> getProducts(int page);

    List<ProductDTO> getSortedProducts(int page, boolean asc, String attribute);

    List<ProductDTO> findProductsByTitle(int page, String title);

    List<ProductDTO> findSortedProductsByTitle(int page, boolean asc, String attribute, String title);
}
