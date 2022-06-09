package com.onlinestore.onlinestore.controller;

import com.onlinestore.onlinestore.constants.ErrorMessage;
import com.onlinestore.onlinestore.constants.SuccessMessage;
import com.onlinestore.onlinestore.dto.request.ProductAddToFavouritesDto;
import com.onlinestore.onlinestore.dto.request.ProductDeleteFromFavouritesDto;
import com.onlinestore.onlinestore.dto.request.UserFavouritesClearDto;
import com.onlinestore.onlinestore.dto.request.UserIdPageNumberDto;
import com.onlinestore.onlinestore.dto.response.ErrorMessageDto;
import com.onlinestore.onlinestore.dto.response.SuccessMessageDto;
import com.onlinestore.onlinestore.exception.*;
import com.onlinestore.onlinestore.service.FavouritesService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/favourites")
@RequiredArgsConstructor
public class FavouritesController {
    private final FavouritesService favouritesService;

    @PostMapping(value = "/add-product")
    public ResponseEntity addProductToFavourites(@RequestBody ProductAddToFavouritesDto productAddToBasketDto) {
        try {
            favouritesService.addProductToFavourites(productAddToBasketDto);

            return new ResponseEntity(
                    new SuccessMessageDto(SuccessMessage.PRODUCT_ADDED),
                    HttpStatus.OK
            );
        } catch (UserNotFoundException e) {

            return new ResponseEntity(
                    new ErrorMessageDto(ErrorMessage.USER_NOT_FOUND),
                    HttpStatus.BAD_REQUEST
            );
        } catch (ProductNotFoundException e) {

            return new ResponseEntity(
                    new ErrorMessageDto(ErrorMessage.PRODUCT_NOT_FOUND),
                    HttpStatus.BAD_REQUEST
            );
        } catch (ProductAlreadyInFavouritesException e) {

            return new ResponseEntity(
                    new ErrorMessageDto(ErrorMessage.PRODUCT_ALREADY_IN_FAVOURITES),
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
    public ResponseEntity getFavouritesProductsPage(@RequestBody UserIdPageNumberDto user) {
        try {

            return new ResponseEntity(
                    favouritesService.getFavouritesProductsPage(user),
                    HttpStatus.OK
            );
        } catch (FavouritesIsEmptyException e) {

            return new ResponseEntity(
                    new ErrorMessageDto(ErrorMessage.FAVOURITES_IS_EMPTY),
                    HttpStatus.BAD_REQUEST
            );
        } catch (RuntimeException e) {

            return new ResponseEntity(
                    new ErrorMessageDto(ErrorMessage.INTERNAL_SERVER_ERROR),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    @PostMapping(value = "/get-products-id")
    public ResponseEntity getFavouritesProductsIdPage(@RequestBody UserIdPageNumberDto user) {
        try {

            return new ResponseEntity(
                    favouritesService.getFavouritesProductsIdPage(user),
                    HttpStatus.OK
            );
        } catch (FavouritesIsEmptyException e) {

            return new ResponseEntity(
                    new ErrorMessageDto(ErrorMessage.FAVOURITES_IS_EMPTY),
                    HttpStatus.BAD_REQUEST
            );
        } catch (RuntimeException e) {

            return new ResponseEntity(
                    new ErrorMessageDto(ErrorMessage.INTERNAL_SERVER_ERROR),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }


    @PostMapping(value = "/delete-product")
    public ResponseEntity deleteProductFromFavourites(@RequestBody ProductDeleteFromFavouritesDto productDto) {
        try {
            favouritesService.deleteProductFromFavourites(productDto);

            return new ResponseEntity(
                    new SuccessMessageDto(SuccessMessage.PRODUCT_DELETED),
                    HttpStatus.OK
            );
        } catch (ProductNotInFavouritesException e) {

            return new ResponseEntity(
                    new ErrorMessageDto(ErrorMessage.PRODUCT_NOT_IN_FAVOURITES),
                    HttpStatus.BAD_REQUEST
            );
        } catch (RuntimeException e) {

            return new ResponseEntity(
                    new ErrorMessageDto(ErrorMessage.INTERNAL_SERVER_ERROR),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    @PostMapping(value = "/clear-favourites")
    public ResponseEntity clearFavourites(@RequestBody UserFavouritesClearDto userFavouritesClearDto) {
        try {
            favouritesService.clearFavourites(userFavouritesClearDto);

            return new ResponseEntity(
                    new SuccessMessageDto(SuccessMessage.FAVOURITES_IS_EMPTIED),
                    HttpStatus.OK
            );
        } catch (FavouritesIsEmptyException e) {

            return new ResponseEntity(
                    new ErrorMessageDto(ErrorMessage.FAVOURITES_IS_EMPTY),
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
