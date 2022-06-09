package com.onlinestore.onlinestore.dao;

import com.onlinestore.onlinestore.embeddable.FavouritesId;
import com.onlinestore.onlinestore.entity.Favourites;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FavouritesDAO extends CrudRepository<Favourites, Long> {
    boolean existsByFavouritesId(FavouritesId favouritesId);
    List<Favourites> findAllByFavouritesIdUserId(Long id, Pageable pageable);
    List<Favourites> findFavouritesEntityByFavouritesIdUserId(Long id);
}
