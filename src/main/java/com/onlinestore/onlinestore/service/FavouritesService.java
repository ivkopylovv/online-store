package com.onlinestore.onlinestore.service;

import com.onlinestore.onlinestore.constants.ErrorMessage;
import com.onlinestore.onlinestore.constants.ProductOption;
import com.onlinestore.onlinestore.dto.request.*;
import com.onlinestore.onlinestore.dto.response.ProductIdDto;
import com.onlinestore.onlinestore.dto.response.ProductInfoDto;
import com.onlinestore.onlinestore.embeddable.FavouritesId;
import com.onlinestore.onlinestore.entity.Favourites;
import com.onlinestore.onlinestore.entity.Product;
import com.onlinestore.onlinestore.exception.*;
import com.onlinestore.onlinestore.repository.FavouritesRepository;
import com.onlinestore.onlinestore.repository.ProductRepository;
import com.onlinestore.onlinestore.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FavouritesService {
    private final FavouritesRepository favouritesRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    public Favourites addProductToFavourites(ProductAddToFavouritesDto productDto) {
        if (!userRepository.existsById(productDto.getUserId())) {
            throw new UserNotFoundException(ErrorMessage.USER_NOT_FOUND);
        }

        if (!productRepository.existsById(productDto.getProductId())) {
            throw new ProductNotFoundException(ErrorMessage.PRODUCT_NOT_FOUND);
        }

        if (favouritesRepository.existsByFavouritesId(new FavouritesId(productDto.getUserId(), productDto.getProductId()))) {
            throw new ProductAlreadyInFavouritesException(ErrorMessage.PRODUCT_ALREADY_IN_FAVOURITES);
        }

        Favourites favouritesEntity = new Favourites
                (new FavouritesId(productDto.getUserId(), productDto.getProductId()));

        return favouritesRepository.save(favouritesEntity);
    }

    public ArrayList<ProductInfoDto> getPageOfProductsFromFavourites(UserIdPageNumberDto userIdPageNumberDto) {
        List<Long> productIds = favouritesRepository.
                findAllByFavouritesIdUserId(
                        userIdPageNumberDto.getUserId(),
                        PageRequest.of(
                                userIdPageNumberDto.getPageNumber(),
                                ProductOption.PAGE_COUNT)).
                stream().map(Favourites::getFavouritesId).map(FavouritesId::getProductId).
                collect(Collectors.collectingAndThen(Collectors.toList(), result -> {
                    if (result.isEmpty()) {
                        new FavouritesIsEmptyException(ErrorMessage.FAVOURITES_IS_EMPTY);
                    }
                    return result;
                }));

        ArrayList <ProductInfoDto> products = new ArrayList<>();

        for (Long productId : productIds) {
            Product product = productRepository.
                    findById(productId).get();
            products.add(new ProductInfoDto(
                    product.getId(),
                    product.getName(),
                    product.getPrice(),
                    product.getImage()));
        }

        return products;
    }

    public ArrayList<ProductIdDto> getPageOfProductsIdFromFavourites(UserIdPageNumberDto user) {
        List<Favourites> favouritesEntities = favouritesRepository.
                findAllByFavouritesIdUserId(user.getUserId(), PageRequest.of(user.getPageNumber(), ProductOption.PAGE_COUNT)).
                stream().
                collect(Collectors.collectingAndThen(Collectors.toList(), result -> {
                    if (result.isEmpty()) {
                        new FavouritesIsEmptyException(ErrorMessage.FAVOURITES_IS_EMPTY);
                    }
                    return result;
                }));

        ArrayList <ProductIdDto> products = new ArrayList<>();

        for (Favourites favouritesEntity: favouritesEntities) {
            Product productEntity = productRepository.findById(
                    favouritesEntity.getFavouritesId().getProductId()).get();
            products.add(new ProductIdDto(productEntity.getId()));
        }

        return products;
    }

    public void deleteProductFromFavourites(ProductDeleteFromFavouritesDto productDto) {
        FavouritesId favouritesId =  new FavouritesId(productDto.getUserId(), productDto.getProductId());

        if (!favouritesRepository.existsByFavouritesId(favouritesId)) {
            throw new ProductNotInFavouritesException(ErrorMessage.PRODUCT_NOT_IN_FAVOURITES);
        }

        Favourites favourites  = new Favourites(favouritesId);
        favouritesRepository.delete(favourites);
    }

    public void clearFavourites(UserFavouritesClearDto userFavouritesClearDto) {
        List<Favourites> favouritesEntities = favouritesRepository.
                findFavouritesEntityByFavouritesIdUserId(userFavouritesClearDto.getUserId()).
                stream().
                collect(Collectors.collectingAndThen(Collectors.toList(), result -> {
                    if (result.isEmpty()) {
                        throw new FavouritesIsEmptyException(ErrorMessage.FAVOURITES_IS_EMPTY);
                    }
                    return result;
                }));

        for (Favourites favouritesEntity: favouritesEntities) {
            favouritesRepository.delete(favouritesEntity);
        }
    }
}
