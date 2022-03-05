package com.onlinestore.onlinestore.service;

import com.onlinestore.onlinestore.entity.UserEntity;
import com.onlinestore.onlinestore.exception.UserAlreadyExistException;
import com.onlinestore.onlinestore.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public UserEntity registration(UserEntity user) throws UserAlreadyExistException {
        if (userRepository.findByName(user.getName()) != null) {
            throw new UserAlreadyExistException("User with that name already exists");

        }
        if (userRepository.findByName(user.getLogin()) != null) {
            throw new UserAlreadyExistException("User with that login already exists");
        }
        return userRepository.save(user);
    }
}

