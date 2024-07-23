package com.kosta.ems.config.jwt;

import com.kosta.ems.studentLogin.exception.ExpiredTokenException;
import com.kosta.ems.studentLogin.exception.InvalidTokenException;
import io.jsonwebtoken.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.time.Duration;
import java.util.*;

import static com.kosta.ems.studentLogin.exception.ExceptionMessage.EXPIRED_TOKEN;
import static com.kosta.ems.studentLogin.exception.ExceptionMessage.INVALID_TOKEN;

import jakarta.servlet.http.Cookie;


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

    public String createAccessToken(String hrdNetId, String studentId) {
        Date now = new Date();

        return Jwts.builder()
                .claim("id", hrdNetId)
                .setSubject(hrdNetId)
                .claim("studentId", studentId)
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

    public TokenInfo createJwt(String hrdNetId, String studentId) {

        String refreshToken = createRefreshToken(hrdNetId);
        String accessToken = createAccessToken(hrdNetId, studentId);

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
            throw new ExpiredTokenException(EXPIRED_TOKEN.getMessage());
        }
        return true;
    }


    public Authentication getAuthentication(String accessToken) {
        Claims claims = parseClaims(accessToken);
        Set<SimpleGrantedAuthority> authorities = Collections.singleton(new SimpleGrantedAuthority("ROLE_STUDENT"));

        log.info("claims {} :", claims);

        if (claims.get("id") == null) {
            // 권한 정보 없는 토큰
            log.error("토큰 권한 없음");
            throw new InvalidTokenException(INVALID_TOKEN.getMessage());
        }

        log.info("claim 권한 정보: {}", authorities);
        log.info("claims.getSubject() {} ", claims.getSubject());

        // Claim에 저장된 사용자 아이디를 통해 UserDetails 객체를 생성
        UserDetails principal = new User(claims.getSubject(), "", authorities);

        log.info("userDetails: {} ", principal);

        return new UsernamePasswordAuthenticationToken(principal, accessToken, authorities);
    }

    public String getHrdNetId(HttpServletRequest request) {
        log.info("request {}", request);
        String accessToken = getAccessToken(request);

        Claims claims = parseClaims(accessToken);

        log.info("id type: {}", claims.get("id").getClass().getName());
        log.info("claims: {}", claims);

        String hrdNetId = (String) claims.get("id");

        log.info("hrdNetId: {}", hrdNetId);
        return hrdNetId;
    }

    public String getStudentId(HttpServletRequest request){
        log.info("request {}", request);
        String accessToken = getAccessToken(request);

        Claims claims = parseClaims(accessToken);

        log.info("?? {}", (String) claims.get("studentId"));
        log.info("???? {}", claims);

        return (String) claims.get("studentId");
    }

    private Claims parseClaims(String accessToken) {
        try {
            return Jwts.parser()
                    .setSigningKey(SECRET_KEY)
                    .build()
                    .parseClaimsJws(accessToken)
                    .getBody();
        } catch (ExpiredJwtException e) {
            throw new ExpiredTokenException(EXPIRED_TOKEN.getMessage());
        }
    }

    public String getAccessToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(HttpHeaders.AUTHORIZATION);

        log.info("bearerToken : {}", bearerToken);
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

    public String getCookieValue(HttpServletRequest request, String name) {
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if (cookie.getName().equals(name)) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

}

