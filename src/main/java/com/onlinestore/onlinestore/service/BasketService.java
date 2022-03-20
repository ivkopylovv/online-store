package com.onlinestore.onlinestore.service;

import com.onlinestore.onlinestore.constants.ErrorMessage;
import com.onlinestore.onlinestore.constants.ProductOption;
import com.onlinestore.onlinestore.dto.request.ProductAddToBasketDto;
import com.onlinestore.onlinestore.dto.request.ProductDeleteFromBasketDto;
import com.onlinestore.onlinestore.dto.request.UserBasketClearDto;
import com.onlinestore.onlinestore.dto.request.UserIdPageNumberDto;
import com.onlinestore.onlinestore.dto.response.ProductNameIdPriceDto;
import com.onlinestore.onlinestore.entity.ProductEntity;
import com.onlinestore.onlinestore.utility.embeddable.BasketId;
import com.onlinestore.onlinestore.entity.BasketEntity;
import com.onlinestore.onlinestore.exception.*;
import com.onlinestore.onlinestore.repository.BasketRepository;
import com.onlinestore.onlinestore.repository.ProductRepository;
import com.onlinestore.onlinestore.repository.UserRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class BasketService {

    private final BasketRepository basketRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public BasketService(BasketRepository basketRepository, ProductRepository productRepository, UserRepository userRepository) {
        this.basketRepository = basketRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    public BasketEntity addProductToBasket(ProductAddToBasketDto product) {
        if (!userRepository.existsById(product.getUserId())) {
            throw new UserNotFoundException(ErrorMessage.USER_NOT_FOUND);
        }

        if (!productRepository.existsById(product.getProductId())) {
            throw new ProductNotFoundException(ErrorMessage.PRODUCT_NOT_FOUND);
        }

        if (basketRepository.existsByBasketId(new BasketId(product.getUserId(), product.getProductId()))) {
            throw new ProductAlreadyInBasketException(ErrorMessage.PRODUCT_ALREADY_IN_BASKET);
        }

        BasketEntity basket = new BasketEntity(new BasketId(product.getUserId(), product.getProductId()));

        return basketRepository.save(basket);
    }

    public ArrayList<ProductNameIdPriceDto> getPageOfProductsFromBasket(UserIdPageNumberDto user) {
        ArrayList<BasketEntity> basketEntities = basketRepository.
                findAllByBasketIdUserId(user.getUserId(), PageRequest.of(user.getPageNumber(), ProductOption.countPage));
        if (basketEntities.isEmpty()) {
            throw new BasketIsEmpty(ErrorMessage.BASKET_IS_EMPTY);
        }

        ArrayList <ProductNameIdPriceDto> products = new ArrayList<>();

        for (BasketEntity basketEntity: basketEntities) {
            ProductEntity productEntity = productRepository.
                    findById(basketEntity.getBasketId().getProductId()).get();
            products.add(new ProductNameIdPriceDto(productEntity.getId(),
                    productEntity.getName(), productEntity.getPrice()));
        }

        return products;
    }

    public void deleteProductFromBasket(ProductDeleteFromBasketDto product) {
        if (!basketRepository.existsByBasketId(new BasketId(product.getUserId(), product.getProductId()))) {
            throw new ProductNotInBasket(ErrorMessage.PRODUCT_NOT_IN_BASKET);
        }

        BasketEntity basket = new BasketEntity(new BasketId(product.getUserId(), product.getProductId()));

        basketRepository.delete(basket);
    }

    public void clearBasket(UserBasketClearDto userBasketClearDto) {

        ArrayList<BasketEntity> basketEntities = basketRepository.findBasketEntityByBasketIdUserId(userBasketClearDto.getUserId());

        if  (basketEntities.isEmpty()) {
            throw new BasketIsEmpty(ErrorMessage.BASKET_IS_EMPTY);
        }

        for (BasketEntity basketEntity: basketEntities) {
            basketRepository.delete(basketEntity);
        }
    }

}