package com.onlinestore.onlinestore.service;

import com.onlinestore.onlinestore.constants.ErrorMessage;
import com.onlinestore.onlinestore.entity.Token;
import com.onlinestore.onlinestore.entity.User;
import com.onlinestore.onlinestore.repository.TokenRepository;
import com.onlinestore.onlinestore.repository.UserRepository;
import com.onlinestore.onlinestore.utility.DateHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TokenService {
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;

    public void saveRefreshToken(String login, String refreshToken) {
        User user = userRepository.findByLogin(login).
                orElseThrow(() -> new UsernameNotFoundException(ErrorMessage.USER_NOT_FOUND));

        Token prevToken = user.getToken();

        if (prevToken != null) {
            user.setToken(null);
            userRepository.save(user);
            tokenRepository.delete(tokenRepository.findById(prevToken.getTokenId()).get());
        }

        Token token = new Token();
        token.setToken(refreshToken);
        token.setExpiredInDate(DateHelper.getRefreshTokenTimeAlive());
        token.setUser(user);

        user.setToken(token);
        tokenRepository.save(token);
        userRepository.save(user);
    }
}
