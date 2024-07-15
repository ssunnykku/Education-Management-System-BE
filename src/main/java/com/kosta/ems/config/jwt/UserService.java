package com.kosta.ems.config.jwt;

import com.kosta.ems.student.StudentMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final StudentMapper studentMapper;

    @Transactional
    public TokenInfo login(String hrdNetId, String password) {
        try {
            // 1. id, password 기반 Authentication 객체 생성, 해당 객체는 인증 여부를 확인하는 authenticated 값이 false.
//            UsernamePasswordAuthenticationToken authenticationToken =
//                    new UsernamePasswordAuthenticationToken(hrdNetId, password);
//
//            log.info("authenticationToken {}", authenticationToken);

            /*요기가 안되는 중*/
            // 2. 검증 진행 - CustomUserDetailsService.loadUserByUsername 메서드가 실행됨
//            Authentication authentication = authenticationManagerBuilder.getObject()
//                    .authenticate(authenticationToken);
//
//            log.info("authentication {} ", authentication);

            TokenInfo token = jwtTokenProvider.createJwt(hrdNetId);

            // 3. Mapper 로그인 처리
            studentMapper.studentLogin(hrdNetId, password);

            // 4. refreshToken 저장
            studentMapper.setRefreshToken(hrdNetId, token.getRefreshToken());

            log.info("token = {} ", token);

            return token;

        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return null;

    }


}
