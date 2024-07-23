package com.kosta.ems.config.jwt;

import com.kosta.ems.studentLogin.exception.ExceptionMessage;
import com.kosta.ems.studentLogin.UserService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;


import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Slf4j
class UserServiceTest {
    @Autowired
    UserService userService;
    @Autowired
    JwtTokenProvider jwtTokenProvider;

    @Test
    void login() {

    }

   
    @Test
    void logout() {
        assertThatThrownBy(() -> {
            userService.logout("aa", "bb");
        }).isInstanceOf(IllegalArgumentException.class).hasMessageContaining(ExceptionMessage.AUTHENTICATION_FAILED.getMessage());
    }
}

