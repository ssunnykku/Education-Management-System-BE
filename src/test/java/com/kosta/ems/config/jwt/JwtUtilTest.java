package com.kosta.ems.config.jwt;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Slf4j
class JwtUtilTest {
    @Value("${jwt.secret}")
    private String SECRET;
    @Value("${jwt.access.expire}")
    private String ACCESS_EXPIRE;

    @Test
    void createJwt() {
        log.info("{}", JwtUtil.createJwt("syc1234", SECRET, ACCESS_EXPIRE));

    }

    @Test
    void isExpired() {
    }
}