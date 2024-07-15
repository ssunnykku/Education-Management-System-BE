package com.kosta.ems.config.jwt;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Slf4j
class UserServiceTest {
    @Autowired
    UserService userService;

    @Test
    void login() {
        log.info("{}", userService.login("syc1234", "1234"));
    }
}