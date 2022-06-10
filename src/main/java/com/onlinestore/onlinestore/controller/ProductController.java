package com.onlinestore.onlinestore.controller;

import com.onlinestore.onlinestore.constants.ErrorMessage;
import com.onlinestore.onlinestore.constants.SuccessMessage;
import com.onlinestore.onlinestore.dto.request.ProductFullInfoAddUpdateDto;
import com.onlinestore.onlinestore.dto.response.CountDto;
import com.onlinestore.onlinestore.dto.response.ErrorMessageDto;
import com.onlinestore.onlinestore.dto.response.SuccessMessageDto;
import com.onlinestore.onlinestore.exception.ProductAlreadyExistException;
import com.onlinestore.onlinestore.exception.ProductImagesNotFoundException;
import com.onlinestore.onlinestore.exception.ProductNotFoundException;
import com.onlinestore.onlinestore.exception.ProductTagsNotFoundException;
import com.onlinestore.onlinestore.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @PostMapping(value = "/add-product")
    public ResponseEntity addProduct(ProductFullInfoAddUpdateDto product) {
        try {
            productService.addProduct(product);

            return new ResponseEntity(
                    new SuccessMessageDto(SuccessMessage.PRODUCT_ADDED),
                    HttpStatus.OK
            );
        } catch (ProductAlreadyExistException e) {

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

    @PatchMapping(value = "/update-product")
    public ResponseEntity updateProduct(ProductFullInfoAddUpdateDto product) {
        try {
            productService.updateProductById(product);

            return new ResponseEntity(
                    new SuccessMessageDto(SuccessMessage.PRODUCT_UPDATED),
                    HttpStatus.OK
            );
        } catch (ProductAlreadyExistException e) {

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

    @DeleteMapping(value = "/delete-product", params = {"name"})
    public ResponseEntity deleteProduct(String name) {
        try {
            productService.deleteProduct(name);

            return new ResponseEntity(
                    new SuccessMessageDto(SuccessMessage.PRODUCT_DELETED),
                    HttpStatus.OK
            );
        } catch (ProductNotFoundException e) {

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

    @GetMapping(value = "/get-product", params = {"id"})
    public ResponseEntity getProduct(Long id) {
        try {

            return new ResponseEntity(
                    productService.getProduct(id),
                    HttpStatus.OK
            );
        } catch (ProductNotFoundException | ProductImagesNotFoundException | ProductTagsNotFoundException e) {

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

    @GetMapping(value = "/get-page", params = {"page"})
    public ResponseEntity getProductsPage(int page) {
        try {
            return new ResponseEntity(
                    productService.getPageProducts(page),
                    HttpStatus.OK
            );
        } catch (RuntimeException e) {

            return new ResponseEntity(
                    new ErrorMessageDto(ErrorMessage.INTERNAL_SERVER_ERROR),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }


    @GetMapping(value = "/get-page", params = {"name", "page"})
    public ResponseEntity getProductsPage(String name, int page) {
        try {

            return new ResponseEntity(
                    productService
                            .searchProductsByNameSortingByParameter(name, page, true, "name"),
                    HttpStatus.OK
            );
        } catch (RuntimeException e) {

            return new ResponseEntity(
                    new ErrorMessageDto(ErrorMessage.INTERNAL_SERVER_ERROR),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    @GetMapping(value = "/get-page", params = {"name", "page", "asc"})
    public ResponseEntity getProductsPage(String name, int page, Boolean asc) {
        try {

            return new ResponseEntity(
                    productService
                            .searchProductsByNameSortingByParameter(name, page, asc, "name"),
                    HttpStatus.OK
            );
        } catch (RuntimeException e) {

            return new ResponseEntity(
                    new ErrorMessageDto(ErrorMessage.INTERNAL_SERVER_ERROR),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    @GetMapping(value = "/get-page", params = {"name", "page", "asc", "sorting"})
    public ResponseEntity getProductsPage(String name, int page, Boolean asc, String sorting) {
        try {

            return new ResponseEntity(
                    productService
                            .searchProductsByNameSortingByParameter(name, page, asc, sorting),
                    HttpStatus.OK
            );
        } catch (RuntimeException e) {

            return new ResponseEntity(
                    new ErrorMessageDto(ErrorMessage.INTERNAL_SERVER_ERROR),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    @GetMapping(value = "/count-page")
    public ResponseEntity getProductsCount() {
        try {

            return new ResponseEntity(
                    new CountDto(productService.getCountPagesProductsLikeName()),
                    HttpStatus.OK
            );
        } catch (RuntimeException e) {

            return new ResponseEntity(
                    new ErrorMessageDto(ErrorMessage.INTERNAL_SERVER_ERROR),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    @GetMapping(value = "/count-page", params = {"name"})
    public ResponseEntity getProductsCount(String name) {
        try {

            return new ResponseEntity(
                    new CountDto(productService.getCountPagesProductsLikeName(name)),
                    HttpStatus.OK
            );
        } catch (RuntimeException e) {

            return new ResponseEntity(
                    new ErrorMessageDto(ErrorMessage.INTERNAL_SERVER_ERROR),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }


}

