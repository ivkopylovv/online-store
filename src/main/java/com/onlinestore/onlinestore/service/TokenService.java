package com.onlinestore.onlinestore.service;

import com.onlinestore.onlinestore.constants.ErrorMessage;
import com.onlinestore.onlinestore.dto.response.UserDto;
import com.onlinestore.onlinestore.entity.TokenEntity;
import com.onlinestore.onlinestore.exception.TokenNotFoundException;
import com.onlinestore.onlinestore.repository.TokenRepository;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class TokenService {
    private TokenRepository tokenRepository;

    public TokenService(TokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    public String getRandomToken(){
        Random random = ThreadLocalRandom.current();
        byte[] r = new byte[64];

        random.nextBytes(r);

        return Base64.encodeBase64String(r);
    }

    public String getTokenByUser(UserDto userDto){
        TokenEntity tokenEntity = tokenRepository.findByUserId(userDto.getId());
        if (tokenEntity == null) {
            throw new TokenNotFoundException(ErrorMessage.INTERNAL_SERVER_ERROR);
        }
        return tokenEntity.getToken();
    }
}
