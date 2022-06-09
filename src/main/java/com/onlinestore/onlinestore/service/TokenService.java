package com.onlinestore.onlinestore.service;

import com.onlinestore.onlinestore.constants.ErrorMessage;
import com.onlinestore.onlinestore.dao.TokenDAO;
import com.onlinestore.onlinestore.dao.UserDAO;
import com.onlinestore.onlinestore.entity.Token;
import com.onlinestore.onlinestore.entity.User;
import com.onlinestore.onlinestore.utility.DateHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TokenService {
    private final UserDAO userDAO;
    private final TokenDAO tokenDAO;

    public void saveRefreshToken(String login, String refreshToken) {
        User user = userDAO
                .findByLogin(login)
                .orElseThrow(() -> new UsernameNotFoundException(ErrorMessage.USER_NOT_FOUND));

        Token prevToken = user.getToken();

        if (prevToken != null) {
            user.setToken(null);
            userDAO.save(user);
            tokenDAO.delete(tokenDAO.findById(prevToken.getTokenId()).get());
        }

        Token token = new Token();
        token.setToken(refreshToken);
        token.setExpiredInDate(DateHelper.getRefreshTokenTimeAlive());
        token.setUser(user);

        user.setToken(token);
        tokenDAO.save(token);
        userDAO.save(user);
    }
}
