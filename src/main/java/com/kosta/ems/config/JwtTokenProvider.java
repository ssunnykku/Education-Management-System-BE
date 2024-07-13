package com.kosta.ems.config;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;

import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;


@Slf4j
@Component
public class JwtTokenProvider {

    /*
     * jwt 생성, 유효성 검사
     * */

    private final Key key;

    @Value("${jwt.access-token.expire}")
    private String ACCESS_TOKEN_EXPIRE;
    @Value("${jwt.refresh-token.expire}")
    private String REFRESH_TOKEN_EXPIRE;

    public JwtTokenProvider(@Value("${jwt.secret.key}") String secretKey) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    public TokenInfo generateToken(Collection<? extends GrantedAuthority> authorityInfo, String id) {
        String authorities = authorityInfo.stream()
                .map(GrantedAuthority::getAuthority).collect(Collectors.joining(","));

        long now = (new Date()).getTime();

        // AccessToken 생성
        Date accessTokenExpiresIn = new Date(now + ACCESS_TOKEN_EXPIRE);
        String accessToken = Jwts.builder()
                .setSubject(id)
                .claim("auth", authorities)
                .setExpiration(accessTokenExpiresIn)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        // RefreshToken 생성
        Date refreshTokenExpiresIn = new Date(now + REFRESH_TOKEN_EXPIRE);
        String refreshToken = Jwts.builder()
                .setExpiration(refreshTokenExpiresIn)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        return TokenInfo.builder()
                .grantType("Bearer")
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    public Authentication getAuthentication(String accessToken) {
        Claims claims = parseClaims(accessToken);
        // 권한 정보 확인
        if (claims.get("auth") == null) {
            throw new InvalidTokenException("token error");
        }

        Collection<? extends GrantedAuthority> authorities = Arrays
                .stream(claims.get("auth").toString().split(","))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        UserDetails principal = new User(claims.getSubject(), "", authorities);

        return new UsernamePasswordAuthenticationToken(principal, "", authorities);

    }

    private Claims parseClaims(String accessToken) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(accessToken)
                    .getBody();
        } catch (ExpiredJwtException e) {
            throw new ExpiredTokenException("expire error");
        }
    }

    // 토큰 검증 메서드
    public boolean validateToken(String token) throws TokenException {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (SecurityException | MalformedJwtException | UnsupportedJwtException | IllegalArgumentException e) {
            throw new InvalidTokenException("token error");
        } catch (ExpiredJwtException e) {
            throw new ExpiredTokenException("expire error");
        }

    }

    //    Http Request의 Header로부터 Access Token을 추출
    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");

        log.debug("bearertoken : {}", bearerToken);
        if (StringUtils.hasText(bearerToken)) {
            if (bearerToken.startsWith("Bearer") && bearerToken.length() > 7) {
                int tokenStartIndex = 7;
                return bearerToken.substring(tokenStartIndex);
            }
            throw new MalformedHeaderException("Malformed Header Exception");
        }

        return bearerToken;
    }
}