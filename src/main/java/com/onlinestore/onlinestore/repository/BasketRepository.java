package com.onlinestore.onlinestore.repository;

import com.onlinestore.onlinestore.utility.embeddable.BasketId;
import com.onlinestore.onlinestore.entity.BasketEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;


@Repository
public interface BasketRepository extends CrudRepository<BasketEntity, Long> {
    boolean existsByBasketId(BasketId basketId);
    ArrayList<BasketEntity> findAllByBasketIdUserId(Long id, Pageable pageable);
    ArrayList<BasketEntity> findBasketEntityByBasketIdUserId(Long id);
}
