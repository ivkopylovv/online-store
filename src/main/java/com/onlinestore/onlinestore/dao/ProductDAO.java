package com.onlinestore.onlinestore.dao;

import com.onlinestore.onlinestore.entity.Product;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductDAO extends JpaRepository<Product, Long> {
    List<Product> findByTitleContaining(String title, Pageable pageable);
}
