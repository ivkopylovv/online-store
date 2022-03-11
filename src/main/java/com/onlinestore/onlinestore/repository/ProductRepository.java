package com.onlinestore.onlinestore.repository;

import com.onlinestore.onlinestore.entity.ProductEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;


public interface ProductRepository extends CrudRepository<ProductEntity, Long> {
    ProductEntity findByName(String name);
    List<ProductEntity> getByIdGreaterThanEqualAndIdLessThanEqual(Long id, Long id1);
}
