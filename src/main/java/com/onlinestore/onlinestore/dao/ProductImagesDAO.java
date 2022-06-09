package com.onlinestore.onlinestore.dao;

import com.onlinestore.onlinestore.entity.ProductImages;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductImagesDAO extends CrudRepository<ProductImages, Long> {
    List<ProductImages> findByProductId(Long id);
}
