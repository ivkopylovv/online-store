package com.onlinestore.onlinestore.service;

import com.onlinestore.onlinestore.constants.ErrorMessage;
import com.onlinestore.onlinestore.dto.request.UserLogoutDto;
import com.onlinestore.onlinestore.dto.request.UserLoginDto;
import com.onlinestore.onlinestore.dto.request.UserRegistrationDto;
import com.onlinestore.onlinestore.entity.Role;
import com.onlinestore.onlinestore.entity.TokenEntity;
import com.onlinestore.onlinestore.entity.UserEntity;
import com.onlinestore.onlinestore.exception.*;
import com.onlinestore.onlinestore.dto.response.UserDto;
import com.onlinestore.onlinestore.repository.RoleRepository;
import com.onlinestore.onlinestore.repository.TokenRepository;
import com.onlinestore.onlinestore.repository.UserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

@Service
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final TokenService tokenService;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    UserService(UserRepository userRepository, TokenRepository tokenRepository, TokenService tokenService, RoleRepository roleRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.tokenRepository = tokenRepository;
        this.tokenService = tokenService;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserEntity registerUser(UserRegistrationDto userDto) throws UserAlreadyExistException {
        if (userRepository.findByLogin(userDto.getLogin()) != null) {
            throw new UserAlreadyExistException(ErrorMessage.USER_EXISTS);
        }

        UserEntity user = new UserEntity(userDto.getName(), userDto.getLogin(), userDto.getPassword());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.getRoles().add(new Role(1L, "ROLE_USER"));

        return userRepository.save(user);
    }

    public UserDto getUserInfo(String token) {
        TokenEntity tokenEntity = tokenRepository.findByToken(token);

        if (tokenEntity == null || tokenEntity.getExpiredIn() < new Date().getTime()) {
            throw new InvalidTokenExceptionException(ErrorMessage.UNAUTHORIZED);
        }

        UserEntity user = userRepository.findByTokenId(tokenEntity.getId());

        return new UserDto(user);
    }

    public void logoutUser(UserLogoutDto userLogoutDto) {
        UserEntity user = userRepository.findById(userLogoutDto.getId()).get();

        if (user == null) {
            throw new UserNotFoundException(ErrorMessage.USER_NOT_FOUND);
        }

        TokenEntity token = tokenRepository.findByUserId(userLogoutDto.getId());

        if (token == null) {
            throw new TokenNotFoundException(ErrorMessage.TOKEN_NOT_FOUND);
        }

        user.setToken(null);
        userRepository.save(user);
        tokenRepository.delete(token);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = userRepository.findByLogin(username);

        if (user == null) {
            throw new UsernameNotFoundException(ErrorMessage.USER_NOT_FOUND);
        }

        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        user.getRoles().forEach(role -> {
            authorities.add(new SimpleGrantedAuthority(role.getName()));
        });

        return new org.springframework.security.core.userdetails.User(user.getLogin(), user.getPassword(), authorities);
    }
}

