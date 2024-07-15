package com.kosta.ems.config.jwt;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Value("${jwt.secret}")
    private String SECRET_KEY;
    @Value("${jwt.refresh.expire}")
    private int REFRESH_EXPIRE;
    @Value("${jwt.access.expire}")
    private int ACCESS_EXPIRE;

    public TokenInfo login(String hrdNetId, String password) {

        return JwtProvider.createJwt(hrdNetId, SECRET_KEY, REFRESH_EXPIRE, ACCESS_EXPIRE);
    }


}
