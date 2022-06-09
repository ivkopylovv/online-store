package com.onlinestore.onlinestore.service;

import com.onlinestore.onlinestore.constants.ErrorMessage;
import com.onlinestore.onlinestore.constants.ProductOption;
import com.onlinestore.onlinestore.dao.FavouritesDAO;
import com.onlinestore.onlinestore.dao.ProductDAO;
import com.onlinestore.onlinestore.dao.UserDAO;
import com.onlinestore.onlinestore.dto.request.ProductAddToFavouritesDto;
import com.onlinestore.onlinestore.dto.request.ProductDeleteFromFavouritesDto;
import com.onlinestore.onlinestore.dto.request.UserFavouritesClearDto;
import com.onlinestore.onlinestore.dto.request.UserIdPageNumberDto;
import com.onlinestore.onlinestore.dto.response.ProductIdDto;
import com.onlinestore.onlinestore.dto.response.ProductInfoDto;
import com.onlinestore.onlinestore.embeddable.FavouritesId;
import com.onlinestore.onlinestore.entity.Favourites;
import com.onlinestore.onlinestore.exception.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FavouritesService {
    private final FavouritesDAO favouritesDAO;
    private final UserDAO userDAO;
    private final ProductDAO productDAO;

    public Favourites addProductToFavourites(ProductAddToFavouritesDto productDto) {
        if (!userDAO.existsById(productDto.getUserId())) {
            throw new UserNotFoundException(ErrorMessage.USER_NOT_FOUND);
        }

        if (!productDAO.existsById(productDto.getProductId())) {
            throw new ProductNotFoundException(ErrorMessage.PRODUCT_NOT_FOUND);
        }

        if (favouritesDAO.existsByFavouritesId(new FavouritesId(productDto.getUserId(), productDto.getProductId()))) {
            throw new ProductAlreadyInFavouritesException(ErrorMessage.PRODUCT_ALREADY_IN_FAVOURITES);
        }

        Favourites favouritesEntity = new Favourites
                (new FavouritesId(productDto.getUserId(), productDto.getProductId()));

        return favouritesDAO.save(favouritesEntity);
    }

    public List<ProductInfoDto> getFavouritesProductsPage(UserIdPageNumberDto userIdPageNumberDto) {
        List<Long> productIds = favouritesDAO
                .findAllByFavouritesIdUserId(
                        userIdPageNumberDto.getUserId(),
                        PageRequest.of(
                                userIdPageNumberDto.getPageNumber(),
                                ProductOption.PAGE_COUNT))
                .stream()
                .map(Favourites::getFavouritesId)
                .map(FavouritesId::getProductId)
                .collect(Collectors
                        .collectingAndThen(Collectors.toList(), result -> {
                            if (result.isEmpty()) {
                                new FavouritesIsEmptyException(ErrorMessage.FAVOURITES_IS_EMPTY);
                            }
                            return result;
                        }));

        List<ProductInfoDto> products = productIds
                .stream()
                .map(productId -> productDAO.findById(productId).get())
                .map(product -> new ProductInfoDto(
                        product.getId(),
                        product.getName(),
                        product.getPrice(),
                        product.getImage()))
                .collect(Collectors.toList());


        return products;
    }

    public List<ProductIdDto> getFavouritesProductsIdPage(UserIdPageNumberDto user) {
        List<Favourites> favouritesEntities = favouritesDAO
                .findAllByFavouritesIdUserId(user.getUserId(),
                        PageRequest.of(
                                user.getPageNumber(),
                                ProductOption.PAGE_COUNT))
                .stream().collect(Collectors.
                        collectingAndThen(Collectors.toList(), result -> {
                            if (result.isEmpty()) {
                                new FavouritesIsEmptyException(ErrorMessage.FAVOURITES_IS_EMPTY);
                            }
                            return result;
                        }));

        List<ProductIdDto> products = favouritesEntities
                .stream()
                .map(favourites ->
                        new ProductIdDto(
                                productDAO
                                        .findById(favourites
                                                .getFavouritesId().getProductId()).get().getId()))
                .collect(Collectors.toList());

        return products;
    }

    public void deleteProductFromFavourites(ProductDeleteFromFavouritesDto productDto) {
        FavouritesId favouritesId = new FavouritesId(productDto.getUserId(), productDto.getProductId());

        if (!favouritesDAO.existsByFavouritesId(favouritesId)) {
            throw new ProductNotInFavouritesException(ErrorMessage.PRODUCT_NOT_IN_FAVOURITES);
        }

        Favourites favourites = new Favourites(favouritesId);
        favouritesDAO.delete(favourites);
    }

    public void clearFavourites(UserFavouritesClearDto userFavouritesClearDto) {
        List<Favourites> favouritesEntities = favouritesDAO
                .findFavouritesEntityByFavouritesIdUserId(userFavouritesClearDto.getUserId())
                .stream()
                .collect(Collectors
                        .collectingAndThen(Collectors.toList(), result -> {
                            if (result.isEmpty()) {
                                throw new FavouritesIsEmptyException(ErrorMessage.FAVOURITES_IS_EMPTY);
                            }
                            return result;
                        }));

        favouritesEntities
                .stream()
                .forEach(favouritesEntity ->
                        favouritesDAO.delete(favouritesEntity)
                );
    }
}
