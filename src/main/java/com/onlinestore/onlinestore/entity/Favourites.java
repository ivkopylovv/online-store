package com.onlinestore.onlinestore.entity;

import com.onlinestore.onlinestore.embeddable.FavouritesId;
import lombok.*;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Favourites {
    @EmbeddedId
    private FavouritesId favouritesId;
}
