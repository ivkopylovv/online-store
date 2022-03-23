package com.onlinestore.onlinestore.repository;

import com.onlinestore.onlinestore.entity.ProductImagesEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductImagesRepository extends CrudRepository<ProductImagesEntity, Long> {
    List<ProductImagesEntity> findByProductId(Long id);
}
