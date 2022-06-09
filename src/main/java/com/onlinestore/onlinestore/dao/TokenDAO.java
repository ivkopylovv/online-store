package com.onlinestore.onlinestore.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import com.onlinestore.onlinestore.entity.Token;

import java.util.Optional;

@Repository
public interface TokenDAO extends CrudRepository<Token, Long> {
    Optional<Token> findByToken(String token);
}
