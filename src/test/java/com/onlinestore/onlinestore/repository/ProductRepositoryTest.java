package com.onlinestore.onlinestore.repository;

import com.onlinestore.onlinestore.constants.ProductOption;
import com.onlinestore.onlinestore.entity.Product;
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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    private Product product;

    @AfterEach
    void tearDown() {
        productRepository.deleteAll();
    }

    @BeforeEach
    public void setUp() {
        product = new Product("name", "description","/image/1.jpg", BigDecimal.valueOf(500));
    }

    @Test
    void itShouldFindProductByName() {
        // given
        productRepository.save(product);

        // when
        Optional<Product> actual = productRepository.findByName(product.getName());

        // then
        assertEquals(product, actual.get());
    }

    @Test
    void itShouldFindProductsListByOrderById() {
        // given
        productRepository.save(product);
        Product secondProduct = new Product(
                "name2",
                "description2",
                "/image/1.jpg",
                BigDecimal.valueOf(1000)
        );
        productRepository.save(secondProduct);
        List <Product> expected = new ArrayList<Product>(Arrays.asList(product, secondProduct));

        // when
        List<Product> actual = productRepository.
                findByOrderById(PageRequest.of(0, ProductOption.PAGE_COUNT));

        // then
        assertEquals(expected, actual);
    }

    @Test
    void itShouldFindProductsListByNameStartingWith() {
        // given
        String name = "name";
        productRepository.save(product);
        List <Product> expected = new ArrayList<Product>(Arrays.asList(product));

        // when
        List<Product> actual = productRepository.getByNameStartingWith(
                name,
                PageRequest.of(0, ProductOption.PAGE_COUNT, Sort.by("id").ascending())
        );

        // then
        assertEquals(expected, actual);

    }

    @Test
    void itShouldReturnProductsCountByNameStartingWith() {
        // given
        String name = "name";
        productRepository.save(product);
        Product secondProduct = new Product(
                "name2",
                "description2",
                "/image/1.jpg",
                BigDecimal.valueOf(1000)
        );
        productRepository.save(secondProduct);
        List <Product> expected = new ArrayList<Product>(Arrays.asList(product, secondProduct));

        // when
        Long actual = productRepository.countByNameStartingWith(name);

        // then
        assertEquals(2, actual);
    }

    @Test
    void itShouldUpdateProductNameAndDescriptionAndPriceById() {
        // given
        productRepository.save(product);
        Product secondProduct = new Product(
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
                        Long.valueOf(8)
                );
        Optional<Product> actual = productRepository.findByName("name2");

        // then
        assertEquals(product.getId(), actual.get().getId());
    }
}