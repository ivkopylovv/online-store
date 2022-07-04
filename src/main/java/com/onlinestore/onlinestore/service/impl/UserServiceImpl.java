package com.onlinestore.onlinestore.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.onlinestore.onlinestore.constants.ErrorMessage;
import com.onlinestore.onlinestore.dao.TokenDAO;
import com.onlinestore.onlinestore.dao.UserDAO;
import com.onlinestore.onlinestore.dto.request.UserCredsDTO;
import com.onlinestore.onlinestore.dto.response.ErrorMessageDTO;
import com.onlinestore.onlinestore.entity.Role;
import com.onlinestore.onlinestore.entity.User;
import com.onlinestore.onlinestore.exception.AlreadyExistsException;
import com.onlinestore.onlinestore.exception.ResourceNotFoundException;
import com.onlinestore.onlinestore.service.TokenService;
import com.onlinestore.onlinestore.service.UserService;
import com.onlinestore.onlinestore.utility.DateHelper;
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

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserDetailsService, UserService {
    private final UserDAO userDAO;
    private final BCryptPasswordEncoder passwordEncoder;
    private final TokenDAO tokenDAO;
    private final TokenService tokenService;

    @Override
    public void registerUser(UserCredsDTO userDto) {
        userDAO.findByUsername(userDto.getUsername())
                .ifPresent(result -> {
                    throw new AlreadyExistsException(ErrorMessage.USER_EXISTS);
                });

        User user = new User()
                .setUsername(userDto.getUsername())
                .setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.getRoles().add(new Role(1L, "ROLE_USER"));

        userDAO.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        User user = userDAO.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(ErrorMessage.USER_NOT_FOUND));

        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        user.getRoles().forEach(role -> {
            authorities.add(new SimpleGrantedAuthority(role.getName()));
        });

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                authorities
        );
    }

    @Override
    public User getUser(String username) {
        return userDAO.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorMessage.USER_NOT_FOUND));
    }

    @Override
    public void logoutUser(String authorizationHeader) {
        String refresh_token = authorizationHeader.substring("Bearer ".length());
        String login = TokenHelper.getUserLoginByToken(refresh_token);
        User user = getUser(login);

        userDAO.save(user);
        tokenDAO.delete(tokenDAO.findByToken(refresh_token).get());
    }

    @Override
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String authorizationHeader = request.getHeader(AUTHORIZATION);

        if (TokenHelper.isAuthorizationHeaderValid(authorizationHeader)) {
            successfulRefresh(request, response, authorizationHeader);
        } else {
            throw new ResourceNotFoundException(ErrorMessage.UNAUTHORIZED);
        }
    }

    @Override
    public void successfulRefresh(HttpServletRequest request, HttpServletResponse response, String authorizationHeader) throws IOException {
        String refresh_token = TokenHelper.getTokenByAuthorizationHeader(authorizationHeader);
        String username = TokenHelper.getUserLoginByToken(refresh_token);
        User user = getUser(username);
        Date expiredInDate = tokenDAO.findByToken(refresh_token).get().getExpirationDate();

        if (expiredInDate.before(DateHelper.getCurrentDate())) {
            throw new ResourceNotFoundException(ErrorMessage.TOKEN_NOT_FOUND);
        }

        String access_token = TokenHelper.getAccessToken(user, request);
        String new_refresh_token = TokenHelper.getRefreshToken(user, request);
        tokenService.saveToken(username, new_refresh_token);

        Map<String, String> tokens = new HashMap<>();
        tokens.put("access_token", access_token);
        tokens.put("refresh_token", new_refresh_token);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        new ObjectMapper().writeValue(response.getOutputStream(), tokens);
    }

    public void unsuccessfulRefresh(HttpServletResponse response, Exception e) throws IOException {
        response.setStatus(UNAUTHORIZED.value());
        response.setContentType(MimeTypeUtils.APPLICATION_JSON_VALUE);
        new ObjectMapper().writeValue(response.getOutputStream(), new ErrorMessageDTO(e.getMessage()));
    }
}

