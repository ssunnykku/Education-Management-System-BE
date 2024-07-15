package com.kosta.ems.config.jwt;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Slf4j
class JwtProviderTest {
    @Value("${jwt.secret}")
    private String SECRET;
    @Value("${jwt.access.expire}")
    private int ACCESS_EXPIRE;
    @Value("${jwt.refresh.expire}")
    private int REFRESH_EXPIRE;

    @Test
    void createJwt() {
        log.info("{}", JwtProvider.createJwt("syc1234", SECRET, REFRESH_EXPIRE, ACCESS_EXPIRE));

    }

    @Test
    void isExpired() {
    }
}