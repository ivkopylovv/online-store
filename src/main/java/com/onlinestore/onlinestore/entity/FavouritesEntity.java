package com.onlinestore.onlinestore.entity;

import com.onlinestore.onlinestore.embeddable.FavouritesId;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

@Entity
public class FavouritesEntity {
    @EmbeddedId
    private FavouritesId favouritesId;

    public FavouritesEntity() {
    }

    public FavouritesEntity(FavouritesId favouritesId) {
        this.favouritesId = favouritesId;
    }

    public FavouritesId getFavouritesId() {
        return favouritesId;
    }

    public void setFavouritesId(FavouritesId favouritesId) {
        this.favouritesId = favouritesId;
    }
}
