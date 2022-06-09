package com.onlinestore.onlinestore.dao;

import com.onlinestore.onlinestore.constants.ProductOption;
import com.onlinestore.onlinestore.embeddable.FavouritesId;
import com.onlinestore.onlinestore.entity.Favourites;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

@DataJpaTest
class FavouritesDAOTest {
    @Autowired
    private FavouritesDAO favouritesDAO;

    private Favourites favouritesEntity;
    private FavouritesId favouritesId;

    @AfterEach
    void tearDown() {
        favouritesDAO.deleteAll();
    }

    @BeforeEach
    public void setUp() {
        favouritesId = new FavouritesId(Long.valueOf(2), Long.valueOf(2));
        favouritesEntity = new Favourites(favouritesId);
    }

    @Test
    void itShouldExistsFavouritesEntityByFavouritesId() {
        // given
        favouritesDAO.save(favouritesEntity);

        // when
        boolean actual = favouritesDAO.existsByFavouritesId(favouritesId);

        // then
        assertEquals(true, actual);
    }

    @Test
    void itShouldFindAllByFavouritesIdUserId() {
        // given
        favouritesDAO.save(favouritesEntity);
        List<Favourites> excepted = new ArrayList<>(Arrays.asList(favouritesEntity));

        // when
        List<Favourites> actual = favouritesDAO.
                findAllByFavouritesIdUserId(Long.valueOf(2), PageRequest.of(0, ProductOption.PAGE_COUNT));

        // then
        assertEquals(excepted.get(0).getFavouritesId().getUserId(), actual.get(0).getFavouritesId().getUserId());
    }
}