package com.onlinestore.onlinestore.controller;

import com.onlinestore.onlinestore.entity.ProductEntity;
import com.onlinestore.onlinestore.entity.UserEntity;
import com.onlinestore.onlinestore.exception.ProductAlreadyExistException;
import com.onlinestore.onlinestore.exception.ProductNotFoundException;
import com.onlinestore.onlinestore.exception.UserAlreadyExistException;
import com.onlinestore.onlinestore.service.ProductService;
import org.apache.catalina.connector.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @PostMapping
    public ResponseEntity addProduct(@RequestBody ProductEntity product) {
        try {
            productService.addProduct(product);
            return ResponseEntity.status(HttpStatus.CREATED).body("New product added");
        } catch (ProductAlreadyExistException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Fault");
        }
    }

    @GetMapping(params = {"id"})
    public ResponseEntity getProduct(@RequestParam long id) {
        try {
            return ResponseEntity.ok(productService.getProduct(id));
        } catch (ProductNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Fault");
        }
    }

    @GetMapping(params = {"start", "end"})
    public ResponseEntity getArrayProduct(@RequestParam long start, @RequestParam long end) {
        try {
            return ResponseEntity.ok().body(productService.getProductsBetweenId1Id2(start,end));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Fault");
        }
    }

    @GetMapping
    @RequestMapping("/count")
    public ResponseEntity getCountProducts() {
        try {
            return ResponseEntity.ok().body(productService.getCountProducts());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Fault");
        }
    }

    @DeleteMapping(params = {"id"})
    public ResponseEntity deleteProduct(@RequestParam long id) {
        try {
            productService.deleteProduct(id);
            return ResponseEntity.ok(HttpStatus.OK);
        } catch (ProductNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Fault");
        }
    }
}
