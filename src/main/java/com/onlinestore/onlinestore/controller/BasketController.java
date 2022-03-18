package com.onlinestore.onlinestore.controller;

import com.onlinestore.onlinestore.constants.ErrorMessage;
import com.onlinestore.onlinestore.constants.SuccessMessage;
import com.onlinestore.onlinestore.dto.request.ProductAddToBasketDto;
import com.onlinestore.onlinestore.dto.request.ProductDeleteFromBasketDto;
import com.onlinestore.onlinestore.dto.request.UserBasketClearDto;
import com.onlinestore.onlinestore.dto.request.UserIdDto;
import com.onlinestore.onlinestore.dto.response.ErrorMessageDto;
import com.onlinestore.onlinestore.dto.response.SuccessMessageDto;
import com.onlinestore.onlinestore.exception.*;
import com.onlinestore.onlinestore.service.BasketService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/basket")
public class BasketController {
    private final BasketService basketService;

    public BasketController(BasketService basketService) {
        this.basketService = basketService;
    }

    @RequestMapping("/addition")
    @PostMapping
    public ResponseEntity addProductInBasket(@RequestBody ProductAddToBasketDto productAddToBasketDto) {
        try {
            basketService.addProductToBasket(productAddToBasketDto);

            return new ResponseEntity(
                    new SuccessMessageDto(SuccessMessage.PRODUCT_ADDED),
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
        } catch (ProductAlreadyInBasketException e) {
            e.printStackTrace();

            return new ResponseEntity(
                    new ErrorMessageDto(ErrorMessage.PRODUCT_ALREADY_IN_BASKET),
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

    @RequestMapping("/get-products")
    @PostMapping
    public ResponseEntity getAllProductsFromBasket(@RequestBody UserIdDto user) {
        try {

            return new ResponseEntity(
                    basketService.getAllProductsFromBasket(user),
                    HttpStatus.OK
            );
        } catch (BasketIsEmpty e) {
            e.printStackTrace();

            return new ResponseEntity(
                    new ErrorMessageDto(ErrorMessage.BASKET_IS_EMPTY),
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

    @RequestMapping("/delete-product")
    @PostMapping
    public ResponseEntity deleteProductFromBasket(@RequestBody ProductDeleteFromBasketDto productDeleteFromBasketDto) {
        try {
            basketService.deleteProductFromBasket(productDeleteFromBasketDto);

            return new ResponseEntity(
                    new SuccessMessageDto(SuccessMessage.PRODUCT_DELETED),
                    HttpStatus.OK
            );
        } catch (ProductNotInBasket e) {
            e.printStackTrace();

            return new ResponseEntity(
                    new ErrorMessageDto(ErrorMessage.PRODUCT_NOT_IN_BASKET),
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

    @RequestMapping("/clear-basket")
    @PostMapping
    public ResponseEntity clearBasket(@RequestBody UserBasketClearDto userBasketClearDto) {
        try {
            basketService.clearBasket(userBasketClearDto);

            return new ResponseEntity(
                    new SuccessMessageDto(SuccessMessage.BASKET_IS_EMPTIED),
                    HttpStatus.OK
            );
        } catch (BasketIsEmpty e) {
            e.printStackTrace();

            return new ResponseEntity(
                    new ErrorMessageDto(ErrorMessage.BASKET_IS_EMPTY),
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
