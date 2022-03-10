package com.onlinestore.onlinestore.repository;

import com.onlinestore.onlinestore.entity.UserEntity;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository <UserEntity, Long> {
    UserEntity findByName(String name);
    UserEntity findByLogin(String login);
}
