package com.kosta.ems.config.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.time.Duration;
import java.util.Date;

public class JwtProvider {


    public static TokenInfo createJwt(String hrdNetId, String secretKey, int refreshExpire, int accessExpire) {
        Date now = new Date();

        String refreshToken = Jwts.builder()
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(now.getTime() + Duration.ofDays(refreshExpire).toMillis()))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();

        String accessToken = Jwts.builder()
                .claim("id", hrdNetId)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(now.getTime() + Duration.ofMillis(accessExpire).toMillis()))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();

        return TokenInfo.builder()
                .grantType("Bearer")
                .refreshToken(refreshToken)
                .accessToken(accessToken)
                .build();
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

