package com.onlinestore.onlinestore.repository;

import com.onlinestore.onlinestore.embeddable.BasketId;
import com.onlinestore.onlinestore.entity.BasketEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface BasketRepository extends CrudRepository<BasketEntity, Long> {
    boolean existsByBasketId(BasketId basketId);
    List<BasketEntity> findAllByBasketIdUserId(Long id, Pageable pageable);
    List<BasketEntity> findBasketEntityByBasketIdUserId(Long id);
    Long countByBasketIdUserId(Long id);
}
