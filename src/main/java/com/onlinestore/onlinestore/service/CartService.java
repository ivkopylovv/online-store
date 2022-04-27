package com.onlinestore.onlinestore.service;

import com.onlinestore.onlinestore.constants.ErrorMessage;
import com.onlinestore.onlinestore.constants.ProductOption;
import com.onlinestore.onlinestore.dto.request.ProductAddToCartDto;
import com.onlinestore.onlinestore.dto.request.ProductDeleteFromCart;
import com.onlinestore.onlinestore.dto.request.UserCartClearDto;
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
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CartService {
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public Long addProductToCart(ProductAddToCartDto productAddToCartDto) {
        if (!userRepository.existsById(productAddToCartDto.getUserId())) {
            throw new UserNotFoundException(ErrorMessage.USER_NOT_FOUND);
        }

        if (!productRepository.existsById(productAddToCartDto.getProductId())) {
            throw new ProductNotFoundException(ErrorMessage.PRODUCT_NOT_FOUND);
        }

        if (cartRepository.existsByCartId(new CartId(productAddToCartDto.getUserId(), productAddToCartDto.getProductId()))) {
            throw new ProductAlreadyInCartException(ErrorMessage.PRODUCT_ALREADY_IN_CART);
        }

        Cart cart = new Cart(new CartId(productAddToCartDto.getUserId(), productAddToCartDto.getProductId()));
        cartRepository.save(cart);

        return cartRepository.countByCartIdUserId(productAddToCartDto.getUserId());
    }

    public List<ProductInfoDto> getPageOfProductsFromBasket(UserIdPageNumberDto userIdPageNumberDto) {
            List<Long> productIds = cartRepository.
                findAllByCartIdUserId(
                        userIdPageNumberDto.getUserId(),
                        PageRequest.of(
                                userIdPageNumberDto.getPageNumber(),
                                ProductOption.PAGE_COUNT)).
                stream().map(Cart::getCartId).map(CartId::getProductId).
                collect(Collectors.collectingAndThen(Collectors.toList(), result -> {
                            if (result.isEmpty()) {
                                throw new CartIsEmptyException(ErrorMessage.CART_IS_EMPTY);
                            }
                            return result;
                }));

        List<ProductInfoDto> products = new ArrayList<>();

        for (Long productId : productIds) {
            Product product = productRepository.
                    findById(productId).get();
            products.add(new ProductInfoDto(
                    product.getId(),
                    product.getName(),
                    product.getPrice(),
                    product.getImage()));
        }

        return products;
    }

    public Long deleteProductFromBasket(ProductDeleteFromCart product) {
        CartId cartId = new CartId(product.getUserId(), product.getProductId());

        if (!cartRepository.existsByCartId(cartId)) {
            throw new ProductNotInCartException(ErrorMessage.PRODUCT_NOT_IN_CART);
        }

        Cart cart = new Cart(cartId);
        cartRepository.delete(cart);

        return cartRepository.countByCartIdUserId(product.getUserId());
    }

    public void clearBasket(UserCartClearDto userCartClearDto) {
        List<Cart> cartEntities = cartRepository.
                findByCartIdUserId(userCartClearDto.getUserId()).
                stream().
                collect(Collectors.collectingAndThen(Collectors.toList(), result -> {
                    if (result.isEmpty()) {
                        throw new CartIsEmptyException(ErrorMessage.CART_IS_EMPTY);
                    }
                    return result;
                }));

        for (Cart cart : cartEntities) {
            cartRepository.delete(cart);
        }
    }

}