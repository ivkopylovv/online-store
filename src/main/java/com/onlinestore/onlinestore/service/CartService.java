package com.onlinestore.onlinestore.service;

import com.onlinestore.onlinestore.dto.request.ProductDTO;

import java.util.List;

public interface CartService {
    List<ProductDTO> getCartProducts(String username);

    void addProductToCart(String username, Long productId);

    void deleteProductFromCart(String username, Long productId);

    void clearCart(String username);
}