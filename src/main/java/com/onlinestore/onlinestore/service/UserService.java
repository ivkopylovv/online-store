package com.onlinestore.onlinestore.service;

import com.onlinestore.onlinestore.constants.ErrorMessage;
import com.onlinestore.onlinestore.constants.TokenOption;
import com.onlinestore.onlinestore.dto.request.UserLoginDto;
import com.onlinestore.onlinestore.dto.request.UserRegistrationDto;
import com.onlinestore.onlinestore.entity.TokenEntity;
import com.onlinestore.onlinestore.entity.UserEntity;
import com.onlinestore.onlinestore.exception.InvalidTokenException;
import com.onlinestore.onlinestore.exception.UserAlreadyExistException;
import com.onlinestore.onlinestore.exception.UserLoginPasswordIncorrectException;
import com.onlinestore.onlinestore.dto.response.UserDto;
import com.onlinestore.onlinestore.repository.TokenRepository;
import com.onlinestore.onlinestore.repository.UserRepository;
import com.onlinestore.onlinestore.utility.DateUtils;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final TokenService tokenService;

    UserService(UserRepository userRepository, TokenRepository tokenRepository, TokenService tokenService) {
        this.userRepository = userRepository;
        this.tokenRepository = tokenRepository;
        this.tokenService = tokenService;
    }

    public UserEntity registerUser(UserRegistrationDto userDto) throws UserAlreadyExistException {
        if (userRepository.findByLogin(userDto.getLogin()) != null) {
            throw new UserAlreadyExistException(ErrorMessage.USER_EXISTS);
        }
        UserEntity user = new UserEntity(userDto.getName(), userDto.getLogin(), userDto.getPassword());
        return userRepository.save(user);
    }

    public UserDto authorizeUser(UserLoginDto userLoginDto) throws UserLoginPasswordIncorrectException {
        UserEntity user = userRepository.findByLogin(userLoginDto.getLogin());

        if (user == null || !user.getPassword().equals(userLoginDto.getPassword())) {
            throw new UserLoginPasswordIncorrectException(ErrorMessage.LOGIN_OR_PASSWORD_INCORRECT);
        }

        TokenEntity prevToken = tokenRepository.findByUserId(user.getId());

        if (prevToken != null) {
            user.setToken(null);
            userRepository.save(user);
            tokenRepository.delete(prevToken);
        }

        String token = tokenService.getRandomToken();
        Long expiredIn = DateUtils.getCurrentDateWithOffset(TokenOption.TOKEN_TIME_ALIVE);
        TokenEntity tokenEntity = new TokenEntity(user, token, expiredIn);

        user.setToken(tokenEntity);
        tokenRepository.save(tokenEntity);
        userRepository.save(user);

        return new UserDto(user);
    }

    public UserDto getUserInfo(String token) {
        TokenEntity tokenEntity = tokenRepository.findByToken(token);

        if (tokenEntity == null || tokenEntity.getExpiredIn() < new Date().getTime()) {
            throw new InvalidTokenException(ErrorMessage.UNAUTHORIZED);
        }

        UserEntity user = userRepository.findByTokenId(tokenEntity.getId());

        return new UserDto(user);
    }
}

