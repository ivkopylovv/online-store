package com.onlinestore.onlinestore.service;

import com.onlinestore.onlinestore.entity.UserEntity;
import com.onlinestore.onlinestore.exception.UserAlreadyExistException;
import com.onlinestore.onlinestore.exception.UserLoginPasswordIncorrectException;
import com.onlinestore.onlinestore.exception.UserNotFoundException;
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

        if (userRepository.findByLogin(user.getLogin()) != null) {
            throw new UserAlreadyExistException("User with that login already exists");
        }

        return userRepository.save(user);
    }

    public UserEntity authorization(String login, String password) throws UserLoginPasswordIncorrectException {
        UserEntity user = userRepository.findByLogin(login);
        if (user.getPassword().equals(null) || !user.getPassword().equals(password)) {
            throw new UserLoginPasswordIncorrectException("Password or login are incorrect");
        }
        return user;
    }

    public UserEntity getUser(Long id) throws UserNotFoundException {
        UserEntity user = userRepository.findById(id).get();
        if (user == null) {
            throw new UserNotFoundException("User is not found");
        }
        return user;
    }
}

