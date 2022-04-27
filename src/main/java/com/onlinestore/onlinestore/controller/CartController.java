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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/cart")
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;

    @PostMapping(value = "/add-product")
    public ResponseEntity addProductInBasket(@RequestBody ProductAddToCartDto productAddToCartDto) {
        try {
            Long count = cartService.addProductToCart(productAddToCartDto);

            return new ResponseEntity(
                    new CountDto(count),
                    HttpStatus.OK
            );
        } catch (UserNotFoundException e) {
            e.printStackTrace();

            return new ResponseEntity(
                    new ErrorMessageDto(ErrorMessage.USER_NOT_FOUND),
                    HttpStatus.BAD_REQUEST
            );
        }
        catch (ProductNotFoundException e) {
            e.printStackTrace();

            return new ResponseEntity(
                    new ErrorMessageDto(ErrorMessage.PRODUCT_NOT_FOUND),
                    HttpStatus.BAD_REQUEST
            );
        } catch (ProductAlreadyInCartException e) {
            e.printStackTrace();

            return new ResponseEntity(
                    new ErrorMessageDto(ErrorMessage.PRODUCT_ALREADY_IN_CART),
                    HttpStatus.BAD_REQUEST
            );
        } catch (RuntimeException e) {
            e.printStackTrace();

            return new ResponseEntity(
                    new ErrorMessageDto(ErrorMessage.INTERNAL_SERVER_ERROR),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    @PostMapping(value = "/get-products")
    public ResponseEntity getPageOfProductsFromBasket(@RequestBody UserIdPageNumberDto user) {
        try {

            return new ResponseEntity(
                    cartService.getPageOfProductsFromBasket(user),
                    HttpStatus.OK
            );
        } catch (CartIsEmptyException e) {
            e.printStackTrace();

            return new ResponseEntity(
                    new ErrorMessageDto(ErrorMessage.CART_IS_EMPTY),
                    HttpStatus.BAD_REQUEST
            );
        } catch (RuntimeException e) {
            e.printStackTrace();

            return new ResponseEntity(
                    new ErrorMessageDto(ErrorMessage.INTERNAL_SERVER_ERROR),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    @PostMapping(value = "/delete-product")
    public ResponseEntity deleteProductFromBasket(@RequestBody ProductDeleteFromCart productDeleteFromCart) {
        try {
            Long count = cartService.deleteProductFromBasket(productDeleteFromCart);

            return new ResponseEntity(
                    new CountDto(count),
                    HttpStatus.OK
            );
        } catch (ProductNotInCartException e) {
            e.printStackTrace();

            return new ResponseEntity(
                    new ErrorMessageDto(ErrorMessage.PRODUCT_NOT_IN_CART),
                    HttpStatus.BAD_REQUEST
            );
        } catch (RuntimeException e) {
            e.printStackTrace();

            return new ResponseEntity(
                    new ErrorMessageDto(ErrorMessage.INTERNAL_SERVER_ERROR),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    @PostMapping(value = "/clear-basket")
    public ResponseEntity clearBasket(@RequestBody UserCartClearDto userCartClearDto) {
        try {
            cartService.clearBasket(userCartClearDto);

            return new ResponseEntity(
                    new SuccessMessageDto(SuccessMessage.CART_IS_EMPTIED),
                    HttpStatus.OK
            );
        } catch (CartIsEmptyException e) {
            e.printStackTrace();

            return new ResponseEntity(
                    new ErrorMessageDto(ErrorMessage.CART_IS_EMPTY),
                    HttpStatus.BAD_REQUEST
            );
        } catch (RuntimeException e) {
            e.printStackTrace();

            return new ResponseEntity(
                    new ErrorMessageDto(ErrorMessage.INTERNAL_SERVER_ERROR),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }
}
