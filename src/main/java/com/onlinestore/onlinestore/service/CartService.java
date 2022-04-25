package com.onlinestore.onlinestore.service;

import com.onlinestore.onlinestore.constants.ErrorMessage;
import com.onlinestore.onlinestore.constants.ProductOption;
import com.onlinestore.onlinestore.dto.request.ProductAddToBasketDto;
import com.onlinestore.onlinestore.dto.request.ProductDeleteFromBasketDto;
import com.onlinestore.onlinestore.dto.request.UserBasketClearDto;
import com.onlinestore.onlinestore.dto.request.UserIdPageNumberDto;
import com.onlinestore.onlinestore.dto.response.ProductInfoDto;
import com.onlinestore.onlinestore.entity.Product;
import com.onlinestore.onlinestore.embeddable.CartId;
import com.onlinestore.onlinestore.entity.Cart;
import com.onlinestore.onlinestore.exception.*;
import com.onlinestore.onlinestore.repository.CartRepository;
import com.onlinestore.onlinestore.repository.ProductRepository;
import com.onlinestore.onlinestore.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CartService {
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public Long addProductToBasket(ProductAddToBasketDto product) {
        if (!userRepository.existsById(product.getUserId())) {
            throw new UserNotFoundException(ErrorMessage.USER_NOT_FOUND);
        }

        if (!productRepository.existsById(product.getProductId())) {
            throw new ProductNotFoundException(ErrorMessage.PRODUCT_NOT_FOUND);
        }

        if (cartRepository.existsByCartId(new CartId(product.getUserId(), product.getProductId()))) {
            throw new ProductAlreadyInBasketException(ErrorMessage.PRODUCT_ALREADY_IN_BASKET);
        }

        Cart cart = new Cart(new CartId(product.getUserId(), product.getProductId()));
        cartRepository.save(cart);

        return cartRepository.countByCartIdUserId(product.getUserId());
    }

    public ArrayList<ProductInfoDto> getPageOfProductsFromBasket(UserIdPageNumberDto user) {
        List<Cart> cartEntities = cartRepository.
                findAllByCartIdUserId(user.getUserId(), PageRequest.of(user.getPageNumber(), ProductOption.PAGE_COUNT));

        if (cartEntities.isEmpty()) {
            throw new BasketIsEmptyException(ErrorMessage.BASKET_IS_EMPTY);
        }

        ArrayList <ProductInfoDto> products = new ArrayList<>();

        for (Cart cart : cartEntities) {
            Product productEntity = productRepository.
                    findById(cart.getCartId().getProductId()).get();
            products.add(new ProductInfoDto(
                    productEntity.getId(),
                    productEntity.getName(),
                    productEntity.getPrice(),
                    productEntity.getImage()));
        }

        return products;
    }

    public Long deleteProductFromBasket(ProductDeleteFromBasketDto product) {
        if (!cartRepository.existsByCartId(new CartId(product.getUserId(), product.getProductId()))) {
            throw new ProductNotInBasketException(ErrorMessage.PRODUCT_NOT_IN_BASKET);
        }

        Cart cart = new Cart(new CartId(product.getUserId(), product.getProductId()));

        cartRepository.delete(cart);

        return cartRepository.countByCartIdUserId(product.getUserId());
    }

    public void clearBasket(UserBasketClearDto userBasketClearDto) {

        List<Cart> cartEntities = cartRepository.findBasketEntityByCartIdUserId(userBasketClearDto.getUserId());

        if  (cartEntities.isEmpty()) {
            throw new BasketIsEmptyException(ErrorMessage.BASKET_IS_EMPTY);
        }

        for (Cart cart : cartEntities) {
            cartRepository.delete(cart);
        }
    }

}