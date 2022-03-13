package com.onlinestore.onlinestore.repository;

import com.onlinestore.onlinestore.entity.TokenEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TokenRepository extends CrudRepository<TokenEntity, Long> {
    TokenEntity findByUserId(Long id);
    TokenEntity findByToken(String token);
}
