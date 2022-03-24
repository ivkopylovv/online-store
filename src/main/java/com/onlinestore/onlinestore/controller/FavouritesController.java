package com.onlinestore.onlinestore.controller;

import com.onlinestore.onlinestore.constants.ErrorMessage;
import com.onlinestore.onlinestore.constants.SuccessMessage;
import com.onlinestore.onlinestore.dto.request.*;
import com.onlinestore.onlinestore.dto.response.CountDto;
import com.onlinestore.onlinestore.dto.response.ErrorMessageDto;
import com.onlinestore.onlinestore.dto.response.SuccessMessageDto;
import com.onlinestore.onlinestore.exception.*;
import com.onlinestore.onlinestore.service.FavouritesService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/favourites")
public class FavouritesController {
    private final FavouritesService favouritesService;

    public FavouritesController(FavouritesService favouritesService) {
        this.favouritesService = favouritesService;
    }

    @PostMapping(value = "/addition")
    public ResponseEntity addProductToFavourites(@RequestBody ProductAddToFavouritesDto productAddToBasketDto) {
        try {
            favouritesService.addProductToFavourites(productAddToBasketDto);

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
        } catch (ProductAlreadyInFavouritesException e) {
            e.printStackTrace();

            return new ResponseEntity(
                    new ErrorMessageDto(ErrorMessage.PRODUCT_ALREADY_IN_FAVOURITES),
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
    public ResponseEntity getPageOfProductsFromFavourites(@RequestBody UserIdPageNumberDto user) {
        try {

            return new ResponseEntity(
                    favouritesService.getPageOfProductsFromFavourites(user),
                    HttpStatus.OK
            );
        } catch (FavouritesIsEmptyException e) {
            e.printStackTrace();

            return new ResponseEntity(
                    new ErrorMessageDto(ErrorMessage.FAVOURITES_IS_EMPTY),
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
    public ResponseEntity deleteProductFromFavourites(@RequestBody ProductDeleteFromFavouritesDto productDto) {
        try {
            favouritesService.deleteProductFromFavourites(productDto);

            return new ResponseEntity(
                    new SuccessMessageDto(SuccessMessage.PRODUCT_DELETED),
                    HttpStatus.OK
            );
        } catch (ProductNotInFavouritesException e) {
            e.printStackTrace();

            return new ResponseEntity(
                    new ErrorMessageDto(ErrorMessage.PRODUCT_NOT_IN_FAVOURITES),
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

    @PostMapping(value = "/clear-favourites")
    public ResponseEntity clearFavourites(@RequestBody UserFavouritesClearDto userFavouritesClearDto) {
        try {
            favouritesService.clearFavourites(userFavouritesClearDto);

            return new ResponseEntity(
                    new SuccessMessageDto(SuccessMessage.FAVOURITES_IS_EMPTIED),
                    HttpStatus.OK
            );
        } catch (FavouritesIsEmptyException e) {
            e.printStackTrace();

            return new ResponseEntity(
                    new ErrorMessageDto(ErrorMessage.FAVOURITES_IS_EMPTY),
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
