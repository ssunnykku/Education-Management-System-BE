package com.kosta.ems.config.jwt;

import com.kosta.ems.student.StudentDTO;
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
    public TokenInfo login(StudentDTO studentDTO) {
        try {
            TokenInfo token = jwtTokenProvider.createJwt(studentDTO.getHrdNetId());

            // 3. Mapper 로그인 처리
            studentMapper.studentLogin(studentDTO.getHrdNetId(), studentDTO.getPassword());

            // 4. refreshToken 저장
            studentMapper.setRefreshToken(studentDTO.getHrdNetId(), token.getRefreshToken());

            log.info("token = {} ", token);

            return token;

        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return null;

    }


}
