package com.onlinestore.onlinestore.repository;

import com.onlinestore.onlinestore.entity.ProductTagsEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ProductTagsRepository extends CrudRepository <ProductTagsEntity, Long> {
    List<ProductTagsEntity> findByProductId(Long id);
}