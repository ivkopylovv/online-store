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

    public ArrayList<ProductInfoDto> getPageOfProductsFromFavourites(UserIdPageNumberDto user) {
        List<Favourites> favouritesEntities = favouritesRepository.
                findAllByFavouritesIdUserId(user.getUserId(), PageRequest.of(user.getPageNumber(), ProductOption.PAGE_COUNT));

        if (favouritesEntities.isEmpty()) {
            throw new FavouritesIsEmptyException(ErrorMessage.FAVOURITES_IS_EMPTY);
        }

        ArrayList <ProductInfoDto> products = new ArrayList<>();

        for (Favourites favouritesEntity: favouritesEntities) {
            Product productEntity = productRepository.findById(
                    favouritesEntity.getFavouritesId().getProductId()).get();
            products.add(new ProductInfoDto(
                    productEntity.getId(),
                    productEntity.getName(),
                    productEntity.getPrice(),
                    productEntity.getImage()));
        }

        return products;
    }

    public ArrayList<ProductIdDto> getPageOfProductsIdFromFavourites(UserIdPageNumberDto user) {
        List<Favourites> favouritesEntities = favouritesRepository.
                findAllByFavouritesIdUserId(user.getUserId(), PageRequest.of(user.getPageNumber(), ProductOption.PAGE_COUNT));

        if (favouritesEntities.isEmpty()) {
            throw new FavouritesIsEmptyException(ErrorMessage.FAVOURITES_IS_EMPTY);
        }

        ArrayList <ProductIdDto> products = new ArrayList<>();

        for (Favourites favouritesEntity: favouritesEntities) {
            Product productEntity = productRepository.findById(
                    favouritesEntity.getFavouritesId().getProductId()).get();
            products.add(new ProductIdDto(productEntity.getId()));
        }

        return products;
    }

    public void deleteProductFromFavourites(ProductDeleteFromFavouritesDto productDto) {
        if (!favouritesRepository.existsByFavouritesId(new FavouritesId(productDto.getUserId(), productDto.getProductId()))) {
            throw new ProductNotInFavouritesException(ErrorMessage.PRODUCT_NOT_IN_FAVOURITES);
        }

        Favourites favourites  = new Favourites(new FavouritesId(productDto.getUserId(), productDto.getProductId()));

        favouritesRepository.delete(favourites);
    }

    public void clearFavourites(UserFavouritesClearDto user) {

        List<Favourites> favouritesEntities = favouritesRepository.findFavouritesEntityByFavouritesIdUserId(user.getUserId());

        if  (favouritesEntities.isEmpty()) {
            throw new FavouritesIsEmptyException(ErrorMessage.FAVOURITES_IS_EMPTY);
        }

        for (Favourites favouritesEntity: favouritesEntities) {
            favouritesRepository.delete(favouritesEntity);
        }
    }
}
