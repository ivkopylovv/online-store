package com.onlinestore.onlinestore.controller;

import com.onlinestore.onlinestore.constants.SuccessMessage;
import com.onlinestore.onlinestore.dto.request.ModifyCartDTO;
import com.onlinestore.onlinestore.dto.request.ProductDTO;
import com.onlinestore.onlinestore.dto.response.ProductListDTO;
import com.onlinestore.onlinestore.dto.response.SuccessMessageDTO;
import com.onlinestore.onlinestore.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1")
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;

    @GetMapping("/cart/{username}")
    public ResponseEntity getProducts(@PathVariable("username") String username) {
        List<ProductDTO> products = cartService.getCartProducts(username);

        return ResponseEntity.ok().body(new ProductListDTO(products));
    }

    @PostMapping("/cart")
    public ResponseEntity addProductToCart(@RequestBody ModifyCartDTO modifyCartDTO) {
        cartService.addProductToCart(modifyCartDTO.getUsername(), modifyCartDTO.getProductId());

        return ResponseEntity.ok().body(new SuccessMessageDTO(SuccessMessage.PRODUCT_ADDED));
    }

    @DeleteMapping(value = "/cart/", params = {"username", "id"})
    public ResponseEntity deleteProductFromCart(
            @RequestParam("username") String username,
            @RequestParam("id") Long productId) {
        cartService.deleteProductFromCart(username, productId);

        return ResponseEntity.ok().body(new SuccessMessageDTO(SuccessMessage.PRODUCT_DELETED));
    }

    @DeleteMapping(value = "/cart/{username}")
    public ResponseEntity clearCart(@PathVariable("username") String username) {
        cartService.clearCart(username);

        return ResponseEntity.ok().body(new SuccessMessageDTO(SuccessMessage.CART_IS_EMPTIED));
    }
}
