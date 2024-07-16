package com.kosta.ems.config.jwt;

import io.jsonwebtoken.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;

@Component
@Slf4j
public class JwtTokenProvider {

    @Value("${jwt.secret}")
    private String SECRET_KEY;
    @Value("${jwt.refresh.expire}")
    private int REFRESH_EXPIRE;
    @Value("${jwt.access.expire}")
    private int ACCESS_EXPIRE;
    private final static String HEADER_AUTHORIZATION = "Authorization";

    private final static String TOKEN_PREFIX = "Bearer ";

    public String createAccessToken(String hrdNetId) {
        Date now = new Date();

        return Jwts.builder()
                .claim("id", hrdNetId)
                .setSubject(hrdNetId)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(now.getTime() + Duration.ofMinutes(ACCESS_EXPIRE).toMillis()))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }


    public String createRefreshToken(String hrdNetId) {
        Date now = new Date();
        return Jwts.builder()
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(now.getTime() + Duration.ofDays(REFRESH_EXPIRE).toMillis()))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    public TokenInfo createJwt(String hrdNetId) {

        String refreshToken = createRefreshToken(hrdNetId);
        String accessToken = createAccessToken(hrdNetId);

        return TokenInfo.builder()
                .grantType("Bearer")
                .refreshToken(refreshToken)
                .accessToken(accessToken)
                .build();
    }


    public boolean isValid(String token) throws InvalidTokenException {

        try {
            Jwts.parser()
                    .setSigningKey(SECRET_KEY)
                    .build()
                    .parseClaimsJws(token);

        } catch (ExpiredJwtException e) {
            log.error(e.getMessage());
            throw new ExpiredTokenException(ExceptionMessage.EXPIRED_TOKEN.getMessage());
        }
        return true;
    }

    public Authentication getAuthentication(String accessToken) {
        Claims claims = parseClaims(accessToken);
        Set<SimpleGrantedAuthority> authorities = Collections.singleton(new SimpleGrantedAuthority("STUDENT"));

        log.info("claims {} :", claims);

        if (claims.get("id") == null) {
            // 권한 정보 없는 토큰
            log.error("토큰 권한 없음");
            throw new IllegalArgumentException();
//            throw new InvalidTokenException(INVALID_TOKEN.getMessage());
        }

        log.info("claim 권한 정보: {}", authorities);
        log.info("claims.getSubject() {} ", claims.getSubject());

        // Claim에 저장된 사용자 아이디를 통해 UserDetails 객체를 생성
        UserDetails principal = new User(claims.getSubject(), "", authorities);

        log.info("userDetails: {} ", principal);

        return new UsernamePasswordAuthenticationToken(principal, accessToken, authorities);
    }

    public String getHrdNetId(HttpServletRequest request) {

        String accessToken = getAccessToken(request);
        Claims claims = parseClaims(accessToken);

        return claims.getSubject();
    }

    private Claims parseClaims(String accessToken) {
        try {
            return Jwts.parser()
                    .setSigningKey(SECRET_KEY)
                    .build()
                    .parseClaimsJws(accessToken)
                    .getBody();
        } catch (ExpiredJwtException e) {
            throw new IllegalStateException();
        }
//            throw new ExpiredTokenException(EXPIRED_TOKEN.getMessage());
    }

    public String getAccessToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(HttpHeaders.AUTHORIZATION);

        log.debug("bearertoken : {}", bearerToken);
        if (StringUtils.hasText(bearerToken)) {
            if (bearerToken.startsWith(TOKEN_PREFIX) && bearerToken.length() > TOKEN_PREFIX.length()) {
                int tokenStartIndex = TOKEN_PREFIX.length();
                return bearerToken.substring(tokenStartIndex);
            }
            throw new IllegalStateException();
//            throw new MalformedHeaderException(MALFORMED_HEADER.getMessage());
        }

        return bearerToken;
    }

}

