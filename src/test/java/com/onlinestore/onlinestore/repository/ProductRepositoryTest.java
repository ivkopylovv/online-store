package com.onlinestore.onlinestore.repository;

import com.onlinestore.onlinestore.constants.ProductOption;
import com.onlinestore.onlinestore.entity.ProductEntity;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class ProductRepositoryTest {

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
    void itShouldFindProductByName() {
        // given
        productRepository.save(product);

        // when
        ProductEntity actual = productRepository.findByName(product.getName());

        // then
        assertEquals(product, actual);
    }

    @Test
    void itShouldFindProductsListByOrderById() {
        // given
        productRepository.save(product);
        ProductEntity secondProduct = new ProductEntity(
                "name2",
                "description2",
                "/image/1.jpg",
                BigDecimal.valueOf(1000)
        );
        productRepository.save(secondProduct);
        List <ProductEntity> expected = new ArrayList<ProductEntity>(Arrays.asList(product, secondProduct));

        // when
        List<ProductEntity> actual = productRepository.
                findByOrderById(PageRequest.of(0, ProductOption.countPage));

        // then
        assertEquals(expected, actual);
    }

    @Test
    void itShouldFindProductsListByNameStartingWith() {
        // given
        String name = "name";
        productRepository.save(product);
        List <ProductEntity> expected = new ArrayList<ProductEntity>(Arrays.asList(product));

        // when
        List<ProductEntity> actual = productRepository.getByNameStartingWith(
                name,
                PageRequest.of(0, ProductOption.countPage, Sort.by("id").ascending())
        );

        // then
        assertEquals(expected, actual);

    }

    @Test
    void itShouldReturnProductsCountByNameStartingWith() {
        // given
        String name = "name";
        productRepository.save(product);
        ProductEntity secondProduct = new ProductEntity(
                "name2",
                "description2",
                "/image/1.jpg",
                BigDecimal.valueOf(1000)
        );
        productRepository.save(secondProduct);
        List <ProductEntity> expected = new ArrayList<ProductEntity>(Arrays.asList(product, secondProduct));

        // when
        Long actual = productRepository.countByNameStartingWith(name);

        // then
        assertEquals(2, actual);
    }

    @Test
    void itShouldUpdateProductNameAndDescriptionAndPriceById() {
        // given
        productRepository.save(product);
        ProductEntity secondProduct = new ProductEntity(
                "name2",
                "description2",
                "/image/1.jpg",
                BigDecimal.valueOf(1000)
        );

        // when
        productRepository.updateNameAndDescriptionAndPriceById(
                        secondProduct.getName(),
                        secondProduct.getDescription(),
                        secondProduct.getPrice(),
                        Long.valueOf(7)
                );
        ProductEntity actual = productRepository.findByName("name2");

        // then
        assertEquals(product.getId(), actual.getId());
    }
}