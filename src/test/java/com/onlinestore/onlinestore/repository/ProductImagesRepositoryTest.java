package com.onlinestore.onlinestore.repository;

import com.onlinestore.onlinestore.entity.ProductEntity;
import com.onlinestore.onlinestore.entity.ProductImagesEntity;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

@DataJpaTest
class ProductImagesRepositoryTest {
    @Autowired
    private ProductImagesRepository productImagesRepository;

    @Autowired
    private ProductRepository productRepository;

    private ProductImagesEntity product;

    @AfterEach
    void tearDown() {
        productImagesRepository.deleteAll();
    }

    @BeforeEach
    public void setUp() {
        product = new ProductImagesEntity(Long.valueOf(3), "/images/1.jpg");
    }

    @Test
    void itShouldFindProductByProductId() {
        // given
        ProductEntity productEntity = new ProductEntity();
        productRepository.save(productEntity);
        product.setProduct(productEntity);
        productImagesRepository.save(product);
        List<ProductImagesEntity> expected = new ArrayList<>(Arrays.asList(product));

        // when
        List<ProductImagesEntity> actual = productImagesRepository.findByProductId(productEntity.getId());

        // then
        assertEquals(expected.get(0).getId(),actual.get(0).getId());
    }
}