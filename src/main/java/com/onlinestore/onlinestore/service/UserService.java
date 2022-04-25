package com.onlinestore.onlinestore.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.onlinestore.onlinestore.constants.ErrorMessage;
import com.onlinestore.onlinestore.dto.request.UserRegistrationDto;
import com.onlinestore.onlinestore.dto.response.ErrorMessageDto;
import com.onlinestore.onlinestore.entity.Role;
import com.onlinestore.onlinestore.entity.User;
import com.onlinestore.onlinestore.exception.*;
import com.onlinestore.onlinestore.repository.RoleRepository;
import com.onlinestore.onlinestore.repository.UserRepository;
import com.onlinestore.onlinestore.utility.TokenHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.MimeTypeUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

import static org.springframework.http.HttpStatus.FORBIDDEN;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public User registerUser(UserRegistrationDto userDto) throws UserAlreadyExistException {
        if (userRepository.findByLogin(userDto.getLogin()) != null) {
            throw new UserAlreadyExistException(ErrorMessage.USER_EXISTS);
        }

        User user = new User(
                userDto.getName(),
                userDto.getLogin(),
                userDto.getPassword()
        );

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.getRoles().add(new Role(1L, "ROLE_USER"));

        return userRepository.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        User user = userRepository.findByLogin(login);

        if (user == null) {
            throw new UsernameNotFoundException(ErrorMessage.USER_NOT_FOUND);
        }

        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        user.getRoles().forEach(role -> {
            authorities.add(new SimpleGrantedAuthority(role.getName()));
        });

        return new org.springframework.security.core.userdetails.User(user.getLogin(), user.getPassword(), authorities);
    }

    public User getUser(String login) {
        return userRepository.findByLogin(login);
    }

    public void successfulRefresh(HttpServletRequest request, HttpServletResponse response, String authorizationHeader) throws IOException {
        String refresh_token = authorizationHeader.substring("Bearer ".length());
        Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
        JWTVerifier verifier = JWT.require(algorithm).build();
        DecodedJWT decodedJWT = verifier.verify(refresh_token);
        String login = decodedJWT.getSubject();
        User user = getUser(login);
        String access_token = TokenHelper.getAccessToken(user, request);
        Map<String, String> tokens = new HashMap<>();
        tokens.put("access_token", access_token);
        tokens.put("refresh_token", refresh_token);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        new ObjectMapper().writeValue(response.getOutputStream(), tokens);
    }

    public void unsuccessfulRefresh(HttpServletResponse response, Exception e) throws IOException {
        response.setStatus(FORBIDDEN.value());
        response.setContentType(MimeTypeUtils.APPLICATION_JSON_VALUE);
        new ObjectMapper().writeValue(response.getOutputStream(), new ErrorMessageDto(e.getMessage()));
    }
}

