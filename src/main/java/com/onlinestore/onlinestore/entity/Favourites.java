package com.onlinestore.onlinestore.entity;

import com.onlinestore.onlinestore.embeddable.FavouritesId;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Favourites {
    @EmbeddedId
    private FavouritesId favouritesId;
}
