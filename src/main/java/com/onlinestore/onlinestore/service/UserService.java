package com.onlinestore.onlinestore.service;

import com.onlinestore.onlinestore.dto.request.UserCredsDTO;
import com.onlinestore.onlinestore.entity.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface UserService {
    void registerUser(UserCredsDTO userCredsDTO);

    User getUser(String username);

    void logoutUser(String authorizationHeader);

    void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException;

    void successfulRefresh(HttpServletRequest request, HttpServletResponse response, String authorizationHeader) throws IOException;

    void unsuccessfulRefresh(HttpServletResponse response, Exception e) throws IOException;
}
