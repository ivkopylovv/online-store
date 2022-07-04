package com.onlinestore.onlinestore.service;

public interface TokenService {
    void saveToken(String username, String newRefreshToken);
}
