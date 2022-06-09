package com.onlinestore.onlinestore.dao;

import com.onlinestore.onlinestore.entity.Token;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TokenDAO extends CrudRepository<Token, Long> {
    Optional<Token> findByToken(String token);
}
