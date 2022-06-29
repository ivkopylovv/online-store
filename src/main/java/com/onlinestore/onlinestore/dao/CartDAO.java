package com.onlinestore.onlinestore.dao;

import com.onlinestore.onlinestore.entity.Cart;
import com.onlinestore.onlinestore.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartDAO extends JpaRepository<Cart, User> {
    Optional<Cart> findByUserUsername(String username);

    void deleteByUserUsername(String username);
}
