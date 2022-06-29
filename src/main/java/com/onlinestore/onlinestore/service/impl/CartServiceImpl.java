package com.onlinestore.onlinestore.service.impl;

import com.onlinestore.onlinestore.constants.ErrorMessage;
import com.onlinestore.onlinestore.dao.CartDAO;
import com.onlinestore.onlinestore.dao.ProductDAO;
import com.onlinestore.onlinestore.dao.UserDAO;
import com.onlinestore.onlinestore.dto.request.ProductDTO;
import com.onlinestore.onlinestore.entity.Cart;
import com.onlinestore.onlinestore.entity.Product;
import com.onlinestore.onlinestore.entity.User;
import com.onlinestore.onlinestore.exception.ResourceNotFoundException;
import com.onlinestore.onlinestore.mapper.ProductMapper;
import com.onlinestore.onlinestore.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {
    private final CartDAO cartDAO;
    private final ProductDAO productDAO;
    private final UserDAO userDAO;

    @Override
    public List<ProductDTO> getCartProducts(String username) {
        Cart cart = cartDAO.findByUserUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorMessage.CART_IS_EMPTY));
        return ProductMapper.entityListToDTOList(new ArrayList<>(cart.getProducts()));
    }

    @Override
    public void addProductToCart(String username, Long productId) {
        Product product = productDAO.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorMessage.PRODUCT_NOT_FOUND));
        Optional<Cart> cart = cartDAO.findByUserUsername(username);

        if (cart.isEmpty()) {
            User user = userDAO.findByUsername(username)
                    .orElseThrow(() -> new ResourceNotFoundException(ErrorMessage.USER_NOT_FOUND));
            Set<Product> products = new HashSet<>();
            products.add(product);
            cartDAO.save(new Cart(user, products));
        } else {
            cart.get().getProducts().add(product);
        }
    }

    @Override
    public void deleteProductFromCart(String username, Long productId) {
        Cart cart = cartDAO.findByUserUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorMessage.CART_IS_EMPTY));

        Product product = productDAO.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorMessage.PRODUCT_NOT_FOUND));

        cart.getProducts().remove(product);
    }

    @Override
    public void clearCart(String username) {
        cartDAO.deleteByUserUsername(username);
    }
}
