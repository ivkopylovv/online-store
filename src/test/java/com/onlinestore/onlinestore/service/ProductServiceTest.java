package com.onlinestore.onlinestore.service;

import com.onlinestore.onlinestore.entity.Product;
import com.onlinestore.onlinestore.dao.ProductDAO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
class ProductServiceTest {
    @Autowired
    private ProductDAO productDAO;

    private Product product;

    @AfterEach
    void tearDown() {
        productDAO.deleteAll();

    }

    @BeforeEach
    public void setUp() {
        product = new Product("name", "description","/image/1.jpg", BigDecimal.valueOf(500));
    }

    @Test
    void itShouldAddProduct() {
        // given
        productDAO.save(product);
        // when
        Optional<Product> actual = productDAO.findByName(product.getName());

        // then
        assertEquals(product, actual.get());
    }

    @Test
    void itShouldGetCountPagesProductsLikeName() {
        // given
        productDAO.save(product);
        productDAO.save(new Product(
                "name2",
                "description2",
                "/image/1.jpg",
                BigDecimal.valueOf(500)
                ));

        // when
        Long actual = productDAO.countByNameStartingWith("na");

        // then
        assertEquals(2, actual);
    }
}