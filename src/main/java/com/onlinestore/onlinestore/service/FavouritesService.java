package com.onlinestore.onlinestore.service;

import com.onlinestore.onlinestore.constants.ErrorMessage;
import com.onlinestore.onlinestore.constants.ProductOption;
import com.onlinestore.onlinestore.dto.request.*;
import com.onlinestore.onlinestore.dto.response.ProductInfoDto;
import com.onlinestore.onlinestore.embeddable.FavouritesId;
import com.onlinestore.onlinestore.entity.FavouritesEntity;
import com.onlinestore.onlinestore.entity.ProductEntity;
import com.onlinestore.onlinestore.exception.*;
import com.onlinestore.onlinestore.repository.FavouritesRepository;
import com.onlinestore.onlinestore.repository.ProductRepository;
import com.onlinestore.onlinestore.repository.UserRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class FavouritesService {
    private final FavouritesRepository favouritesRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    public FavouritesService(FavouritesRepository favouritesRepository, UserRepository userRepository, ProductRepository productRepository) {
        this.favouritesRepository = favouritesRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
    }

    public FavouritesEntity addProductToFavourites(ProductAddToFavouritesDto productDto) {
        if (!userRepository.existsById(productDto.getUserId())) {
            throw new UserNotFoundException(ErrorMessage.USER_NOT_FOUND);
        }

        if (!productRepository.existsById(productDto.getProductId())) {
            throw new ProductNotFoundException(ErrorMessage.PRODUCT_NOT_FOUND);
        }

        if (favouritesRepository.existsByFavouritesId(new FavouritesId(productDto.getUserId(), productDto.getProductId()))) {
            throw new ProductAlreadyInFavouritesException(ErrorMessage.PRODUCT_ALREADY_IN_FAVOURITES);
        }

        FavouritesEntity favouritesEntity = new FavouritesEntity
                (new FavouritesId(productDto.getUserId(), productDto.getProductId()));

        return favouritesRepository.save(favouritesEntity);
    }

    public ArrayList<ProductInfoDto> getPageOfProductsFromFavourites(UserIdPageNumberDto user) {
        List<FavouritesEntity> favouritesEntities = favouritesRepository.
                findAllByFavouritesIdUserId(user.getUserId(), PageRequest.of(user.getPageNumber(), ProductOption.countPage));

        if (favouritesEntities.isEmpty()) {
            throw new FavouritesIsEmptyException(ErrorMessage.FAVOURITES_IS_EMPTY);
        }

        ArrayList <ProductInfoDto> products = new ArrayList<>();

        for (FavouritesEntity favouritesEntity: favouritesEntities) {
            ProductEntity productEntity = productRepository.findById(
                    favouritesEntity.getFavouritesId().getProductId()).get();
            products.add(new ProductInfoDto(
                    productEntity.getId(),
                    productEntity.getName(),
                    productEntity.getPrice(),
                    productEntity.getImage()));
        }

        return products;
    }

    public void deleteProductFromFavourites(ProductDeleteFromFavouritesDto productDto) {
        if (!favouritesRepository.existsByFavouritesId(new FavouritesId(productDto.getUserId(), productDto.getProductId()))) {
            throw new ProductNotInFavouritesException(ErrorMessage.PRODUCT_NOT_IN_FAVOURITES);
        }

        FavouritesEntity favourites  = new FavouritesEntity(new FavouritesId(productDto.getUserId(), productDto.getProductId()));

        favouritesRepository.delete(favourites);
    }

    public void clearFavourites(UserFavouritesClearDto user) {

        List<FavouritesEntity> favouritesEntities = favouritesRepository.findFavouritesEntityByFavouritesIdUserId(user.getUserId());

        if  (favouritesEntities.isEmpty()) {
            throw new FavouritesIsEmptyException(ErrorMessage.FAVOURITES_IS_EMPTY);
        }

        for (FavouritesEntity favouritesEntity: favouritesEntities) {
            favouritesRepository.delete(favouritesEntity);
        }
    }
}
