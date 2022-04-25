package com.onlinestore.onlinestore.repository;

import com.onlinestore.onlinestore.entity.ProductTags;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ProductTagsRepository extends CrudRepository <ProductTags, Long> {
    List<ProductTags> findByProductId(Long id);
}