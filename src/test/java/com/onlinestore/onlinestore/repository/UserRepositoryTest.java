package com.onlinestore.onlinestore.repository;

import com.onlinestore.onlinestore.entity.UserEntity;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository underTest;

    @AfterEach
    void tearDown() {
        underTest.deleteAll();
    }

    @Test
    void itShouldFindUserByLogin() {
        // given
        String login = "name@name";
        UserEntity actual = new UserEntity(
                "name",
                login,
                "123456"
        );
        underTest.save(actual);

        // when
        UserEntity excepted = underTest.findByLogin(login);

        // then
        assertThat(actual).isEqualTo(excepted);
    }

    @Test
    void itShouldNotFindUserByLogin() {
        // given
        String login = "name@name";

        // when
        UserEntity excepted = underTest.findByLogin(login);

        // then
        assertThat(excepted).isNull();
    }
}