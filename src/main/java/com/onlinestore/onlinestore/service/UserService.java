package com.onlinestore.onlinestore.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.onlinestore.onlinestore.constants.ErrorMessage;
import com.onlinestore.onlinestore.dao.TokenDAO;
import com.onlinestore.onlinestore.dao.UserDAO;
import com.onlinestore.onlinestore.dto.request.UserRegistrationDto;
import com.onlinestore.onlinestore.dto.response.ErrorMessageDto;
import com.onlinestore.onlinestore.entity.Role;
import com.onlinestore.onlinestore.entity.User;
import com.onlinestore.onlinestore.exception.TokenNotFoundException;
import com.onlinestore.onlinestore.exception.UserAlreadyExistException;
import com.onlinestore.onlinestore.exception.UserNotFoundException;
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
public class UserService implements UserDetailsService {
    private final UserDAO userDAO;
    private final BCryptPasswordEncoder passwordEncoder;
    private final TokenDAO tokenDAO;
    private final TokenService tokenService;

    public void registerUser(UserRegistrationDto userDto) {
        userDAO.findByLogin(userDto.getLogin())
                .ifPresent(result -> {
                    throw new UserAlreadyExistException(ErrorMessage.USER_EXISTS);
                });

        User user = new User(
                userDto.getUsername(),
                userDto.getLogin(),
                userDto.getPassword()
        );

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.getRoles().add(new Role(1L, "ROLE_USER"));
        userDAO.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String login) {
        User user = userDAO.findByLogin(login)
                .orElseThrow(() -> new UsernameNotFoundException(ErrorMessage.USER_NOT_FOUND));

        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        user.getRoles().forEach(role -> {
            authorities.add(new SimpleGrantedAuthority(role.getName()));
        });

        return new org.springframework.security.core.userdetails.User(
                user.getLogin(),
                user.getPassword(),
                authorities
        );
    }

    public User getUser(String login) {
        return userDAO.findByLogin(login)
                .orElseThrow(() -> new UserNotFoundException(ErrorMessage.USER_NOT_FOUND));
    }

    public void logoutUser(String authorizationHeader) {
        String refresh_token = authorizationHeader.substring("Bearer ".length());
        String login = TokenHelper.getUserLoginByToken(refresh_token);
        User user = getUser(login);

        user.setToken(null);
        userDAO.save(user);
        tokenDAO.delete(tokenDAO.findByToken(refresh_token).get());
    }

    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String authorizationHeader = request.getHeader(AUTHORIZATION);

        if (TokenHelper.isAuthorizationHeaderValid(authorizationHeader)) {
            successfulRefresh(request, response, authorizationHeader);
        } else {
            throw new TokenNotFoundException(ErrorMessage.UNAUTHORIZED);
        }
    }

    public void successfulRefresh(HttpServletRequest request, HttpServletResponse response, String authorizationHeader) throws IOException {
        String refresh_token = TokenHelper.getTokenByAuthorizationHeader(authorizationHeader);
        String login = TokenHelper.getUserLoginByToken(refresh_token);
        User user = getUser(login);
        Date expiredInDate = tokenDAO.findByToken(refresh_token).get().getExpiredInDate();

        if (expiredInDate.before(DateHelper.getCurrentDate())) {
            throw new TokenNotFoundException(ErrorMessage.TOKEN_NOT_FOUND);
        }

        String access_token = TokenHelper.getAccessToken(user, request);
        String new_refresh_token = TokenHelper.getRefreshToken(user, request);
        tokenService.saveRefreshToken(login, new_refresh_token);

        Map<String, String> tokens = new HashMap<>();
        tokens.put("access_token", access_token);
        tokens.put("refresh_token", new_refresh_token);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        new ObjectMapper().writeValue(response.getOutputStream(), tokens);
    }

    public void unsuccessfulRefresh(HttpServletResponse response, Exception e) throws IOException {
        response.setStatus(UNAUTHORIZED.value());
        response.setContentType(MimeTypeUtils.APPLICATION_JSON_VALUE);
        new ObjectMapper().writeValue(response.getOutputStream(), new ErrorMessageDto(e.getMessage()));
    }
}

