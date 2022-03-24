package com.onlinestore.onlinestore.service;

import com.onlinestore.onlinestore.entity.TokenEntity;
import com.onlinestore.onlinestore.entity.UserEntity;
import com.onlinestore.onlinestore.repository.TokenRepository;
import com.onlinestore.onlinestore.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
class UserServiceTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TokenRepository tokenRepository;

    private UserEntity user;
    private TokenEntity token;

    @BeforeEach
    public void setUp() {
        user = new UserEntity("name","name@name","123456");
        token = new TokenEntity(user, "token", 500L);
    }

    @AfterEach
    public void tearDown() {
        userRepository.deleteAll();
        user = null;
        tokenRepository.deleteAll();
        token = null;
    }

    @Test
    void itShouldRegisterUser() {
        // given
        userRepository.save(user);

        // when
        UserEntity actual = userRepository.findById(user.getId()).get();

        // then
        assertEquals(5, actual.getId());
    }

    @Test
    void itShouldAuthorizeUser() {
        // given
        userRepository.save(user);
        tokenRepository.save(token);

        // when
        TokenEntity actual = tokenRepository.findByToken(token.getToken());

        // then
        assertEquals("token", actual.getToken());
    }

    @Test
    void itShouldReturnUserInfo() {
        // given
        user.setToken(token);
        userRepository.save(user);
        tokenRepository.save(token);

        // when
        UserEntity actual = userRepository.findByTokenId(token.getId());

        // then
        assertEquals(user, actual);
    }

    @Test
    void itShouldLogoutUser() {
        // given
        user.setToken(null);
        userRepository.save(user);
        tokenRepository.delete(token);

        // when
        TokenEntity actual = tokenRepository.findByUserId(user.getId());

        // then
        assertEquals(null, actual);
    }
}