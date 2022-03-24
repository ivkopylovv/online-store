package com.onlinestore.onlinestore.controller;

import com.onlinestore.onlinestore.constants.ErrorMessage;
import com.onlinestore.onlinestore.constants.SuccessMessage;
import com.onlinestore.onlinestore.dto.request.ProductAllFieldsDto;
import com.onlinestore.onlinestore.dto.response.CountDto;
import com.onlinestore.onlinestore.dto.response.ErrorMessageDto;
import com.onlinestore.onlinestore.dto.response.SuccessMessageDto;
import com.onlinestore.onlinestore.exception.ProductAlreadyExistException;
import com.onlinestore.onlinestore.exception.ProductImagesNotFoundException;
import com.onlinestore.onlinestore.exception.ProductNotFoundException;
import com.onlinestore.onlinestore.exception.ProductTagsNotFoundException;
import com.onlinestore.onlinestore.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping(value = "/add-product")
    public ResponseEntity addProduct(@RequestBody ProductAllFieldsDto product) {
        try {
            productService.addProduct(product);

            return new ResponseEntity(
                    new SuccessMessageDto(SuccessMessage.PRODUCT_ADDED),
                    HttpStatus.OK
            );
        } catch (ProductAlreadyExistException e) {
            e.printStackTrace();

            return new ResponseEntity(
                    new ErrorMessageDto(ErrorMessage.PRODUCT_ALREADY_EXIST),
                    HttpStatus.BAD_REQUEST);
        } catch (RuntimeException e) {
            e.printStackTrace();

            return new ResponseEntity(
                    new ErrorMessageDto(ErrorMessage.INTERNAL_SERVER_ERROR),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PatchMapping(value = "/update-product")
    public ResponseEntity updateProduct(@RequestBody ProductAllFieldsDto product) {
        try {
            productService.updateProductById(product);

            return new ResponseEntity(
                    new SuccessMessageDto(SuccessMessage.PRODUCT_UPDATED),
                    HttpStatus.OK
            );
        } catch (ProductAlreadyExistException e) {
            e.printStackTrace();

            return new ResponseEntity(
                    new ErrorMessageDto(ErrorMessage.PRODUCT_WITH_NAME_ALREADY_EXIST),
                    HttpStatus.BAD_REQUEST);
        } catch (RuntimeException e) {
            e.printStackTrace();

            return new ResponseEntity(
                    new ErrorMessageDto(ErrorMessage.INTERNAL_SERVER_ERROR),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping(value = "/delete-product", params = {"id", "name"})
    public ResponseEntity deleteProduct(@RequestParam long id, @RequestParam String name) {
        try {
            productService.deleteProduct(id);

            return new ResponseEntity(
                    new SuccessMessageDto(SuccessMessage.PRODUCT_DELETED),
                    HttpStatus.OK
            );

        } catch (ProductNotFoundException e) {
            e.printStackTrace();

            return new ResponseEntity(
                    new ErrorMessageDto(ErrorMessage.PRODUCT_NOT_FOUND),
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

    @GetMapping(value = "/get-product", params = {"id"})
    public ResponseEntity getProduct(@RequestParam long id) {
        try {

            return new ResponseEntity(
                    productService.getProduct(id),
                    HttpStatus.OK);
        } catch (ProductNotFoundException e) {
            e.printStackTrace();

            return new ResponseEntity(
                    new ErrorMessageDto(ErrorMessage.PRODUCT_NOT_FOUND),
                    HttpStatus.BAD_REQUEST
            );
        } catch (ProductImagesNotFoundException e) {
            e.printStackTrace();

            return new ResponseEntity(
                    new ErrorMessageDto(ErrorMessage.PRODUCT_IMAGES_NOT_FOUND),
                    HttpStatus.BAD_REQUEST
            );
        } catch (ProductTagsNotFoundException e) {
            e.printStackTrace();

            return new ResponseEntity(
                    new ErrorMessageDto(ErrorMessage.PRODUCT_TAGS_NOT_FOUND),
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

    @GetMapping(value = "/get-page", params = {"page"})
    public ResponseEntity getProductsPage(@RequestParam int page) {
        try {
            return new ResponseEntity(
                    productService.getPageProducts(page),
                    HttpStatus.OK
            );
        } catch (RuntimeException e) {
            e.printStackTrace();

            return new ResponseEntity(
                    new ErrorMessageDto(ErrorMessage.INTERNAL_SERVER_ERROR),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }


    @GetMapping(value = "/get-page", params = {"name", "page"})
    public ResponseEntity getSearchProductsPage(@RequestParam String name, @RequestParam int page) {
        try {
            return new ResponseEntity(
                    productService.searchProductsByNameSortingByParameter(name, page, true, "name"),
                    HttpStatus.OK
            );
        } catch (RuntimeException e) {
            e.printStackTrace();

            return new ResponseEntity(
                    new ErrorMessageDto(ErrorMessage.INTERNAL_SERVER_ERROR),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    @GetMapping(value = "/get-page", params = {"name", "page", "asc"})
    public ResponseEntity getSearchProductsPage(@RequestParam String name,
                                                @RequestParam int page,
                                                @RequestParam Boolean asc) {
        try {
            return new ResponseEntity(
                    productService.searchProductsByNameSortingByParameter(name, page, asc, "name"),
                    HttpStatus.OK
            );
        } catch (RuntimeException e) {
            e.printStackTrace();

            return new ResponseEntity(
                    new ErrorMessageDto(ErrorMessage.INTERNAL_SERVER_ERROR),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    @GetMapping(value = "/get-page", params = {"name", "page", "asc", "sorting"})
    public ResponseEntity getSearchProductsPage(@RequestParam String name,
                                                @RequestParam int page,
                                                @RequestParam Boolean asc,
                                                @RequestParam String sorting) {
        try {
            return new ResponseEntity(
                    productService.searchProductsByNameSortingByParameter(name, page, asc, sorting),
                    HttpStatus.OK
            );
        } catch (RuntimeException e) {
            e.printStackTrace();

            return new ResponseEntity(
                    new ErrorMessageDto(ErrorMessage.INTERNAL_SERVER_ERROR),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    @GetMapping(value = "/count-page")
    public ResponseEntity getCountProducts() {
        try {
            return new ResponseEntity(
                    new CountDto(productService.getCountPagesProductsLikeName()),
                    HttpStatus.OK
            );
        } catch (RuntimeException e) {
            e.printStackTrace();

            return new ResponseEntity(
                    new ErrorMessageDto(ErrorMessage.INTERNAL_SERVER_ERROR),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    @GetMapping(value = "/count-page", params = {"name"})
    public ResponseEntity getCountProducts(@RequestParam String name) {
        try {
            return new ResponseEntity(
                    new CountDto(productService.getCountPagesProductsLikeName(name)),
                    HttpStatus.OK
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

