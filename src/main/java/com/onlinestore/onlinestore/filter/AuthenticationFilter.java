package com.onlinestore.onlinestore.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.onlinestore.onlinestore.utility.TokenHelper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;

    public AuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        return authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getParameter("login"),
                        request.getParameter("password")
                )
        );
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException {
        User user = (User)authentication.getPrincipal();
        String access_token = TokenHelper.getAccessToken(user, request);
        String refresh_token = TokenHelper.getRefreshToken(user, request);

        response.setHeader("access_token", access_token);
        response.setHeader("refresh_token", refresh_token);

        response.setContentType(APPLICATION_JSON_VALUE);
        Map<String, String> successMessage = new HashMap<>();
        successMessage.put("message", "Successful login");
        response.setContentType(APPLICATION_JSON_VALUE);
        new ObjectMapper().writeValue(response.getOutputStream(), successMessage);
    }
}
