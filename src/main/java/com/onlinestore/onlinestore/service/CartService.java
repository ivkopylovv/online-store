package com.onlinestore.onlinestore.service;

import com.onlinestore.onlinestore.constants.ErrorMessage;
import com.onlinestore.onlinestore.constants.ProductOption;
import com.onlinestore.onlinestore.dao.CartDAO;
import com.onlinestore.onlinestore.dao.ProductDAO;
import com.onlinestore.onlinestore.dao.UserDAO;
import com.onlinestore.onlinestore.dto.request.ProductAddToCartDto;
import com.onlinestore.onlinestore.dto.request.ProductDeleteFromCart;
import com.onlinestore.onlinestore.dto.request.UserCartClearDto;
import com.onlinestore.onlinestore.dto.request.UserIdPageNumberDto;
import com.onlinestore.onlinestore.dto.response.ProductInfoDto;
import com.onlinestore.onlinestore.embeddable.CartId;
import com.onlinestore.onlinestore.entity.Cart;
import com.onlinestore.onlinestore.exception.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CartService {
    private final CartDAO cartDAO;
    private final ProductDAO productDAO;
    private final UserDAO userDAO;

    public Long addProductToCart(ProductAddToCartDto productAddToCartDto) {
        if (!userDAO.existsById(productAddToCartDto.getUserId())) {
            throw new UserNotFoundException(ErrorMessage.USER_NOT_FOUND);
        }

        if (!productDAO.existsById(productAddToCartDto.getProductId())) {
            throw new ProductNotFoundException(ErrorMessage.PRODUCT_NOT_FOUND);
        }

        if (cartDAO.existsByCartId(new CartId(productAddToCartDto.getUserId(), productAddToCartDto.getProductId()))) {
            throw new ProductAlreadyInCartException(ErrorMessage.PRODUCT_ALREADY_IN_CART);
        }

        Cart cart = new Cart(new CartId(productAddToCartDto.getUserId(), productAddToCartDto.getProductId()));
        cartDAO.save(cart);

        return cartDAO.countByCartIdUserId(productAddToCartDto.getUserId());
    }

    public List<ProductInfoDto> getCartProductsPage(UserIdPageNumberDto userIdPageNumberDto) {

        return cartDAO
                .findAllByCartIdUserId(
                        userIdPageNumberDto.getUserId(),
                        PageRequest.of(
                                userIdPageNumberDto.getPageNumber(),
                                ProductOption.PAGE_COUNT))
                .stream()
                .map(Cart::getCartId)
                .map(CartId::getProductId)
                .map(productId -> productDAO.findById(productId).get())
                .map(product -> new ProductInfoDto(
                        product.getId(),
                        product.getName(),
                        product.getPrice(),
                        product.getImage()))
                .collect(Collectors
                        .collectingAndThen(Collectors.toList(), result -> {
                            if (result.isEmpty()) {
                                throw new CartIsEmptyException(ErrorMessage.CART_IS_EMPTY);
                            }
                            return result;
                        })
                );
    }

    public Long deleteProductFromCart(ProductDeleteFromCart product) {
        CartId cartId = new CartId(product.getUserId(), product.getProductId());

        if (!cartDAO.existsByCartId(cartId)) {
            throw new ProductNotInCartException(ErrorMessage.PRODUCT_NOT_IN_CART);
        }

        Cart cart = new Cart(cartId);
        cartDAO.delete(cart);

        return cartDAO.countByCartIdUserId(product.getUserId());
    }

    public void clearCart(UserCartClearDto userCartClearDto) {
        cartDAO
                .findByCartIdUserId(userCartClearDto.getUserId())
                .stream()
                .collect(Collectors
                        .collectingAndThen(Collectors.toList(), result -> {
                            if (result.isEmpty())
                                throw new CartIsEmptyException(ErrorMessage.CART_IS_EMPTY);
                            return result;
                        }))
                .forEach(cartEntity ->
                        cartDAO.delete(cartEntity)
                );
    }

}