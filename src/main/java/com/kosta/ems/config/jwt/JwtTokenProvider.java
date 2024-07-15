package com.kosta.ems.config.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
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


    public TokenInfo createJwt(String hrdNetId) {
        Date now = new Date();

        String refreshToken = Jwts.builder()
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(now.getTime() + Duration.ofDays(REFRESH_EXPIRE).toMillis()))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();

        String accessToken = Jwts.builder()
                .claim("id", hrdNetId)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(now.getTime() + Duration.ofMillis(ACCESS_EXPIRE).toMillis()))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();

        return TokenInfo.builder()
                .grantType("Bearer")
                .refreshToken(refreshToken)
                .accessToken(accessToken)
                .build();
    }


    public boolean isValid(String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(SECRET_KEY)
                    .build()
                    .parseClaimsJws(token)
                    .getBody()
                    .getExpiration()
                    .before(new Date());
        } catch (Exception e) {
            return false;
        }
    }

    public Authentication getAuthentication(String accessToken) {
        Claims claims = parseClaims(accessToken);

        if (claims.get("auth") == null) {
            // 권한 정보 없는 토큰
            throw new IllegalArgumentException();
//            throw new InvalidTokenException(INVALID_TOKEN.getMessage());
        }

        // Claim에서 권한 정보를 추출한다.
        Collection<? extends GrantedAuthority> authorities = Arrays
                .stream(claims.get("auth").toString().split(","))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        // Claim에 저장된 사용자 아이디를 통해 UserDetails 객체를 생성
        UserDetails principal = new User(claims.getSubject(), "", authorities);

        return new UsernamePasswordAuthenticationToken(principal, "", authorities);
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


//    public Long getUserId(String token) {
//        Claims claims = getClaims(token);
//        return claims.get("id", Long.class);
//    }


}

