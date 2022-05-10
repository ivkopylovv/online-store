package com.onlinestore.onlinestore.utility;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.onlinestore.onlinestore.entity.Role;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import javax.servlet.http.HttpServletRequest;
import java.util.stream.Collectors;

public class TokenHelper {
    private static final String SECRET_KEY = "secret";
    private static final String CLAIMS_NAME = "roles";

    public static Algorithm getToken() {
        return Algorithm.HMAC256(SECRET_KEY.getBytes());
    }

    public static String getAccessToken(User user, HttpServletRequest request) {

        return JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(DateHelper.getAccessTokenTimeAlive())
                .withIssuer(request.getRequestURL().toString())
                .withClaim(CLAIMS_NAME, user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                .sign(getToken());
    }

    public static String getAccessToken(com.onlinestore.onlinestore.entity.User user, HttpServletRequest request) {

        return JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(DateHelper.getAccessTokenTimeAlive())
                .withIssuer(request.getRequestURL().toString())
                .withClaim(CLAIMS_NAME, user.getRoles().stream().map(Role::getName).collect(Collectors.toList()))
                .sign(getToken());
    }

    public static String getRefreshToken(User user, HttpServletRequest request) {

        return JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(DateHelper.getRefreshTokenTimeAlive())
                .withIssuer(request.getRequestURL().toString())
                .sign(getToken());
    }

    public static String getRefreshToken(com.onlinestore.onlinestore.entity.User user, HttpServletRequest request) {

        return JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(DateHelper.getRefreshTokenTimeAlive())
                .withIssuer(request.getRequestURL().toString())
                .sign(getToken());
    }

    public static String getTokenByAuthorizationHeader(String authorizationHeader) {
        return authorizationHeader.substring("Bearer ".length());
    }

    public static String getUserLoginByToken(String token) {
        Algorithm algorithm = getToken();
        JWTVerifier verifier = JWT.require(algorithm).build();
        DecodedJWT decodedJWT = verifier.verify(token);
        String login = decodedJWT.getSubject();

        return login;
    }

    public static boolean isAuthorizationHeaderValid(String authorizationHeader) {
        return authorizationHeader != null && authorizationHeader.startsWith("Bearer ");
    }


}
