package com.onlinestore.onlinestore.dao;

import com.onlinestore.onlinestore.constants.ProductOption;
import com.onlinestore.onlinestore.entity.Product;
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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
class ProductDAOTest {

    @Autowired
    private ProductDAO productDAO;

    private Product product;

    @AfterEach
    void tearDown() {
        productDAO.deleteAll();
    }

    @BeforeEach
    public void setUp() {
        product = new Product("name", "description", "/image/1.jpg", Double.valueOf(500));
    }

    @Test
    void itShouldFindProductByName() {
        // given
        productDAO.save(product);

        // when
        Optional<Product> actual = productDAO.findByName(product.getName());

        // then
        assertEquals(product, actual.get());
    }

    @Test
    void itShouldFindProductsListByOrderById() {
        // given
        productDAO.save(product);
        Product secondProduct = new Product(
                "name2",
                "description2",
                "/image/1.jpg",
                Double.valueOf(1000)
        );
        productDAO.save(secondProduct);
        List<Product> expected = new ArrayList<Product>(Arrays.asList(product, secondProduct));

        // when
        List<Product> actual = productDAO.
                findByOrderById(PageRequest.of(0, ProductOption.PAGE_COUNT));

        // then
        assertEquals(expected, actual);
    }

    @Test
    void itShouldFindProductsListByNameStartingWith() {
        // given
        String name = "name";
        productDAO.save(product);
        List<Product> expected = new ArrayList<Product>(Arrays.asList(product));

        // when
        List<Product> actual = productDAO.getByNameStartingWith(
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
        productDAO.save(product);
        Product secondProduct = new Product(
                "name2",
                "description2",
                "/image/1.jpg",
                Double.valueOf(1000)
        );
        productDAO.save(secondProduct);
        List<Product> expected = new ArrayList<Product>(Arrays.asList(product, secondProduct));

        // when
        Long actual = productDAO.countByNameStartingWith(name);

        // then
        assertEquals(2, actual);
    }
}