package com.kosta.ems.config.jwt;

import com.kosta.ems.studentLogin.exception.ExceptionMessage;
import com.kosta.ems.studentLogin.exception.InvalidTokenException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.nio.file.AccessDeniedException;

@RequiredArgsConstructor
@Slf4j
public class TokenAuthenticationFilter extends OncePerRequestFilter {
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        try {


            String token = jwtTokenProvider.getAccessToken((HttpServletRequest) request);

            log.info("authorization = " + token);
            String requestURI = request.getRequestURI();

            if (!requestURI.startsWith("/api")) {
                filterChain.doFilter(request, response);
                return;
            }
            log.info("여기");
            log.info("url: {}", requestURI);

            // 로그인 요청일 경우 토큰 검증을 생략한다.
            if ("/api/students/login".equals(requestURI)) {
                filterChain.doFilter(request, response);
                return;
            }

            if (token == null) {
                throw new AccessDeniedException("권한이 없습니다.");
            }

            // 유효성 검사
            if (token != null && jwtTokenProvider.isValid(token)) {
                // Token이 유효할 경우, Authentication 객체를 생성하여 SecurityContext에 저장한다.
                Authentication authentication = jwtTokenProvider.getAuthentication(token);

                log.info("유효성 검사 통과 {} ", authentication);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }

            log.info("res {} ", response);

            filterChain.doFilter(request, response);

        } catch (AccessDeniedException e) {
            log.error(e.getMessage());
            throw new InvalidTokenException(ExceptionMessage.AUTHORIZATION_FAILED.getMessage());


        }


    }
}
