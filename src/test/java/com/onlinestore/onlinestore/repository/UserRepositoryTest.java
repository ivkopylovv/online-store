package com.onlinestore.onlinestore.repository;
/*
import com.onlinestore.onlinestore.entity.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TokenRepository tokenRepository;

    private User user;
    private TokenEntity token;

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
        tokenRepository.deleteAll();
    }

    @BeforeEach
    public void setUp() {
        user = new User("name","name@name","123456");
        token = new TokenEntity(user, "token", 500L);
    }

    @Test
    void itShouldFindUserByLogin() {
        // given
        String login = "name@name";
        userRepository.save(user);

        // when
        User actual = userRepository.findByLogin(login);

        // then
        assertThat(user).isEqualTo(actual);
    }

    @Test
    void itShouldNotFindUserByLogin() {
        // given
        String login = "name@name";

        // when
        User excepted = userRepository.findByLogin(login);

        // then
        assertThat(excepted).isNull();
    }

    @Test
    void itShouldCheckThatUserExists() {
        // given
        userRepository.save(user);
        tokenRepository.save(token);

        // when
        boolean actual = userRepository.existsById(user.getId());

        // then
        assertThat(actual).isNotNull();
    }

    @Test
    void itShouldFindUserByTokenId() {
        // given
        user.setToken(token);
        userRepository.save(user);
        tokenRepository.save(token);

        // when
        User actual = userRepository.findByTokenId(token.getId());

        // then
        assertEquals(user, actual);
    }
}*/