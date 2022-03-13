package com.onlinestore.onlinestore.repository;

import com.onlinestore.onlinestore.entity.UserEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository <UserEntity, Long> {
    UserEntity findByLogin(String login);
    UserEntity findByTokenId(Long tokenId);
}
