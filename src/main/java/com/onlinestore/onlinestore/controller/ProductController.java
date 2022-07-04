package com.onlinestore.onlinestore.controller;

import com.onlinestore.onlinestore.dto.request.ProductDTO;
import com.onlinestore.onlinestore.dto.response.ProductListDTO;
import com.onlinestore.onlinestore.entity.Product;
import com.onlinestore.onlinestore.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("api/v1")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @GetMapping(value = "/products/{id}")
    public ResponseEntity getProduct(@PathVariable("id") Long id) {
        Product product = productService.getProduct(id);

        return ResponseEntity.ok().body(product);
    }

    @GetMapping(value = "/products", params = {"page"})
    public ResponseEntity getProducts(@RequestParam("page") int page) {
        List<ProductDTO> products = productService.getProducts(page);

        return ResponseEntity.ok().body(new ProductListDTO(products));
    }

    @GetMapping(value = "/products", params = {"page, asc, attribute"})
    public ResponseEntity getSortedProducts(
            @RequestParam("page") int page,
            @RequestParam("asc") boolean asc,
            @RequestParam("attribute") String attribute) {
        List<ProductDTO> products = productService.getSortedProducts(page, asc, attribute);

        return ResponseEntity.ok().body(new ProductListDTO(products));
    }

    @GetMapping(value = "/products", params = {"page, title"})
    public ResponseEntity findProductsByTitle(
            @RequestParam("page") int page,
            @RequestParam("title") String title) {
        List<ProductDTO> products = productService.findProductsByTitle(page, title);

        return ResponseEntity.ok().body(new ProductListDTO(products));
    }

    @GetMapping(value = "/products", params = {"page, asc, attribute, title"})
    public ResponseEntity findSortedProductsByTitle(
            @RequestParam("page") int page,
            @RequestParam("asc") boolean asc,
            @RequestParam("attribute") String attribute,
            @RequestParam("title") String title) {
        List<ProductDTO> products = productService.findSortedProductsByTitle(page, asc, attribute, title);

        return ResponseEntity.ok().body(new ProductListDTO(products));
    }
}
