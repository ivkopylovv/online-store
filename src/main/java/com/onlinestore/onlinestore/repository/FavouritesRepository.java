package com.onlinestore.onlinestore.repository;

import com.onlinestore.onlinestore.embeddable.FavouritesId;
import com.onlinestore.onlinestore.entity.BasketEntity;
import com.onlinestore.onlinestore.entity.FavouritesEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FavouritesRepository extends CrudRepository<FavouritesEntity, Long> {
    boolean existsByFavouritesId(FavouritesId favouritesId);
    List<FavouritesEntity> findAllByFavouritesIdUserId(Long id, Pageable pageable);
    List<FavouritesEntity> findFavouritesEntityByFavouritesIdUserId(Long id);
}
