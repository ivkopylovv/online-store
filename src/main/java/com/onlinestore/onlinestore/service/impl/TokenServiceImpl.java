package com.onlinestore.onlinestore.service.impl;

import com.onlinestore.onlinestore.dao.TokenDAO;
import com.onlinestore.onlinestore.dao.UserDAO;
import com.onlinestore.onlinestore.entity.Token;
import com.onlinestore.onlinestore.entity.User;
import com.onlinestore.onlinestore.exception.UnauthorizedException;
import com.onlinestore.onlinestore.service.TokenService;
import com.onlinestore.onlinestore.utility.DateHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.onlinestore.onlinestore.constants.ErrorMessage.UNAUTHORIZED;

@Service
@Transactional
@RequiredArgsConstructor
public class TokenServiceImpl implements TokenService {
    private final TokenDAO tokenDAO;
    private final UserDAO userDAO;

    @Override
    public void saveToken(String username, String newRefreshToken) {
        Optional<Token> optionalToken = tokenDAO.findByUserUsername(username);
        User user;

        if (optionalToken.isPresent()) {
            Token token = optionalToken.get();
            tokenDAO.delete(token);
            user = token.getUser();
        } else {
            user = userDAO.findByUsername(username)
                    .orElseThrow(() -> new UnauthorizedException(UNAUTHORIZED));
        }

        Token newToken = new Token(null, newRefreshToken, user, DateHelper.getAccessTokenTimeAlive());
        tokenDAO.save(newToken);
    }
}