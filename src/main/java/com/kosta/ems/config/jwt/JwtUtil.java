package com.kosta.ems.config.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.time.Duration;
import java.util.Date;

public class JwtUtil {


    public static String createJwt(String hrdNetId, String secretKey, String expiredMs) {
        Date now = new Date();
        return Jwts.builder()
                .claim("id", hrdNetId)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(now.getTime() + Duration.ofDays(14).toMillis()))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    public static boolean isExpired(String token, String secretKey) {
        return Jwts.parser()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getExpiration()
                .before(new Date());
    }
}

