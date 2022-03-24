package com.onlinestore.onlinestore.service;

import com.onlinestore.onlinestore.entity.ProductEntity;
import com.onlinestore.onlinestore.entity.ProductImagesEntity;
import com.onlinestore.onlinestore.entity.ProductTagsEntity;
import com.onlinestore.onlinestore.repository.ProductImagesRepository;
import com.onlinestore.onlinestore.repository.ProductRepository;
import com.onlinestore.onlinestore.repository.ProductTagsRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
class ProductServiceTest {
    @Autowired
    private ProductRepository productRepository;

    private ProductEntity product;

    @AfterEach
    void tearDown() {
        productRepository.deleteAll();

    }

    @BeforeEach
    public void setUp() {
        product = new ProductEntity("name", "description","/image/1.jpg", BigDecimal.valueOf(500));
    }

    @Test
    void itShouldAddProduct() {
        // given
        productRepository.save(product);
        // when
        ProductEntity actual = productRepository.findByName(product.getName());

        // then
        assertEquals(product, actual);
    }

    @Test
    void itShouldGetCountPagesProductsLikeName() {
        // given
        productRepository.save(product);
        productRepository.save(new ProductEntity(
                "name2",
                "description2",
                "/image/1.jpg",
                BigDecimal.valueOf(500)
                ));

        // when
        Long actual = productRepository.countByNameStartingWith("na");

        // then
        assertEquals(2, actual);
    }
}