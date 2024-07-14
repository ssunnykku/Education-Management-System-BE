package com.kosta.ems.config.jwt;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Value("${jwt.secret}")
    private String SECRET_KEY;
    @Value("${jwt.access.expire}")
    private String EXPIRE;

    public String login(String hrdNetId, String password) {

        return JwtUtil.createJwt(hrdNetId, SECRET_KEY, EXPIRE);
    }


}
