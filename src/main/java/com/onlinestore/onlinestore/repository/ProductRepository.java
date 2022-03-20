package com.onlinestore.onlinestore.repository;

import com.onlinestore.onlinestore.entity.ProductEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface ProductRepository extends CrudRepository<ProductEntity, Long> {
    ProductEntity findByName(String name);

    List<ProductEntity> findByOrderById(Pageable pageable);

    List<ProductEntity> getByNameStartingWith(String name, Pageable pageable);

    long countByNameStartingWith(String name);

    @Transactional
    @Modifying
    @Query("update ProductEntity p set p.name = ?1, p.description = ?2, p.price = ?3 where p.id = ?4")
    void updateNameAndDescriptionAndPriceById(String name, String description, BigDecimal price, Long id);
}
