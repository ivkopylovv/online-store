package com.onlinestore.onlinestore.repository;
/*
import com.onlinestore.onlinestore.embeddable.CartId;
import com.onlinestore.onlinestore.entity.Cart;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

@DataJpaTest
class CartRepositoryTest {

    @Autowired
    private BasketRepository basketRepository;

    private Cart cart;
    private CartId cartId;

    @AfterEach
    void tearDown() {
        basketRepository.deleteAll();
    }

    @BeforeEach
    public void setUp() {
        cartId = new CartId(Long.valueOf(1), Long.valueOf(1));
        cart = new Cart(cartId);
    }

    @Test
    void itShouldCheckThatBasketExistsEntityByBasketId() {
        // given
        basketRepository.save(cart);

        // when
        boolean actual = basketRepository.existsByBasketId(cartId);

        // then
        assertEquals(true, actual);
    }

    @Test
    void itShouldFindAllEntitiesByBasketIdUserId() {
        // given
        basketRepository.save(cart);
        List<Cart> excepted = new ArrayList<Cart>();

        // when
        List <Cart> actual = basketRepository.
                findBasketEntityByBasketIdUserId(Long.valueOf(3));

        // then
        assertEquals(excepted, actual);
    }

    @Test
    void itShouldReturnCountByBasketIdUserId() {
        // given
        basketRepository.save(cart);
        CartId secondCartId = new CartId(Long.valueOf(2), Long.valueOf(2));
        Cart secondCart = new Cart(secondCartId);
        basketRepository.save(secondCart);

        // when
        Long actual = basketRepository.countByBasketIdUserId(Long.valueOf(2));

        // then
        assertEquals(Long.valueOf(1), actual);
    }
}*/