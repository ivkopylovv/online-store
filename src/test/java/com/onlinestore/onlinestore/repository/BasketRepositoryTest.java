package com.onlinestore.onlinestore.repository;

import com.onlinestore.onlinestore.constants.ProductOption;
import com.onlinestore.onlinestore.embeddable.BasketId;
import com.onlinestore.onlinestore.entity.BasketEntity;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

@DataJpaTest
class BasketRepositoryTest {

    @Autowired
    private BasketRepository basketRepository;

    private BasketEntity basket;
    private BasketId basketId;

    @AfterEach
    void tearDown() {
        basketRepository.deleteAll();
    }

    @BeforeEach
    public void setUp() {
        basketId = new BasketId(Long.valueOf(1), Long.valueOf(1));
        basket = new BasketEntity(basketId);
    }

    @Test
    void itShouldCheckThatBasketExistsEntityByBasketId() {
        // given
        basketRepository.save(basket);

        // when
        boolean actual = basketRepository.existsByBasketId(basketId);

        // then
        assertEquals(true, actual);
    }

    @Test
    void itShouldFindAllEntitiesByBasketIdUserId() {
        // given
        basketRepository.save(basket);
        List<BasketEntity> excepted = new ArrayList<BasketEntity>();

        // when
        List <BasketEntity> actual = basketRepository.
                findBasketEntityByBasketIdUserId(Long.valueOf(3));

        // then
        assertEquals(excepted, actual);
    }

    @Test
    void itShouldReturnCountByBasketIdUserId() {
        // given
        basketRepository.save(basket);
        BasketId secondBasketId = new BasketId(Long.valueOf(2), Long.valueOf(2));
        BasketEntity secondBasket = new BasketEntity(secondBasketId);
        basketRepository.save(secondBasket);

        // when
        Long actual = basketRepository.countByBasketIdUserId(Long.valueOf(2));

        // then
        assertEquals(Long.valueOf(1), actual);
    }
}