package com.onlinestore.onlinestore.repository;

import com.onlinestore.onlinestore.embeddable.CartId;
import com.onlinestore.onlinestore.entity.Cart;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartRepository extends CrudRepository<Cart, Long> {
    boolean existsByCartId(CartId cartId);
    List<Cart> findAllByCartIdUserId(Long id, Pageable pageable);
    List<Cart> findByCartIdUserId(Long id);
    Long countByCartIdUserId(Long id);
}
