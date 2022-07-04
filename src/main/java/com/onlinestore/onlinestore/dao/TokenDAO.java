package com.onlinestore.onlinestore.dao;

import com.onlinestore.onlinestore.entity.Token;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TokenDAO extends JpaRepository<Token, Long> {
    Optional<Token> findByUserUsername(String username);

    Optional<Token> findByToken(String token);
}
