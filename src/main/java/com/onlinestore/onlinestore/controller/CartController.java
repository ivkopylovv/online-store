package com.onlinestore.onlinestore.controller;

import com.onlinestore.onlinestore.constants.ErrorMessage;
import com.onlinestore.onlinestore.constants.SuccessMessage;
import com.onlinestore.onlinestore.dto.request.ProductAddToCartDto;
import com.onlinestore.onlinestore.dto.request.ProductDeleteFromCart;
import com.onlinestore.onlinestore.dto.request.UserCartClearDto;
import com.onlinestore.onlinestore.dto.request.UserIdPageNumberDto;
import com.onlinestore.onlinestore.dto.response.CountDto;
import com.onlinestore.onlinestore.dto.response.ErrorMessageDto;
import com.onlinestore.onlinestore.dto.response.SuccessMessageDto;
import com.onlinestore.onlinestore.exception.*;
import com.onlinestore.onlinestore.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/cart")
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;

    @PostMapping(value = "/add-product")
    public ResponseEntity addProductToCart(ProductAddToCartDto productAddToCartDto) {
        try {
            Long count = cartService.addProductToCart(productAddToCartDto);

            return new ResponseEntity(
                    new CountDto(count),
                    HttpStatus.OK
            );
        } catch (UserNotFoundException | ProductNotFoundException | ProductAlreadyInCartException e) {

            return new ResponseEntity(
                    new ErrorMessageDto(e.getMessage()),
                    HttpStatus.BAD_REQUEST
            );
        } catch (RuntimeException e) {

            return new ResponseEntity(
                    new ErrorMessageDto(ErrorMessage.INTERNAL_SERVER_ERROR),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    @PostMapping(value = "/get-products")
    public ResponseEntity getCartProductsPage(UserIdPageNumberDto user) {
        try {

            return new ResponseEntity(
                    cartService.getCartProductsPage(user),
                    HttpStatus.OK
            );
        } catch (CartIsEmptyException e) {

            return new ResponseEntity(
                    new ErrorMessageDto(e.getMessage()),
                    HttpStatus.BAD_REQUEST
            );
        } catch (RuntimeException e) {

            return new ResponseEntity(
                    new ErrorMessageDto(ErrorMessage.INTERNAL_SERVER_ERROR),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    @DeleteMapping(value = "/delete-product")
    public ResponseEntity deleteProductFromCart(ProductDeleteFromCart productDeleteFromCart) {
        try {
            Long count = cartService.deleteProductFromCart(productDeleteFromCart);

            return new ResponseEntity(
                    new CountDto(count),
                    HttpStatus.OK
            );
        } catch (ProductNotInCartException e) {

            return new ResponseEntity(
                    new ErrorMessageDto(e.getMessage()),
                    HttpStatus.BAD_REQUEST
            );
        } catch (RuntimeException e) {

            return new ResponseEntity(
                    new ErrorMessageDto(ErrorMessage.INTERNAL_SERVER_ERROR),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    @PostMapping(value = "/clear-cart")
    public ResponseEntity clearCart(UserCartClearDto userCartClearDto) {
        try {
            cartService.clearCart(userCartClearDto);

            return new ResponseEntity(
                    new SuccessMessageDto(SuccessMessage.CART_IS_EMPTIED),
                    HttpStatus.OK
            );
        } catch (CartIsEmptyException e) {

            return new ResponseEntity(
                    new ErrorMessageDto(e.getMessage()),
                    HttpStatus.BAD_REQUEST
            );
        } catch (RuntimeException e) {

            return new ResponseEntity(
                    new ErrorMessageDto(ErrorMessage.INTERNAL_SERVER_ERROR),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }
}
