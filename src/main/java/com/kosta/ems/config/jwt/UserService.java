package com.kosta.ems.config.jwt;

import com.kosta.ems.student.StudentDTO;
import com.kosta.ems.student.StudentMapper;
import io.jsonwebtoken.Claims;
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

    public TokenInfo isRefreshTokenValid(String refreshToken) {

        String hrdNetId = studentMapper.findByToken(refreshToken);

        if (hrdNetId != null && jwtTokenProvider.isValid(refreshToken)) {

            /*      check refreshToken -> 유효한 경우 access만 재발급*/
            return TokenInfo.builder().accessToken(jwtTokenProvider.createAccessToken(hrdNetId)).refreshToken(refreshToken).build();
        } else if (!jwtTokenProvider.isValid(refreshToken)) {

            /*            check refereshToken : 만료되었거나 DB에 없으면 access, refresh 둘 다 재발행*/
            String newRefreshToken = jwtTokenProvider.createRefreshToken(hrdNetId);

            log.info("1 {} ", newRefreshToken);

            //   DB 업데이트하기
            studentMapper.setRefreshToken(hrdNetId, newRefreshToken);
            log.info("2 {} ", newRefreshToken);

            return TokenInfo.builder().accessToken(jwtTokenProvider.createAccessToken(hrdNetId)).refreshToken(newRefreshToken).build();

        } else {
            throw new IllegalArgumentException(ExceptionMessage.INVALID_TOKEN.getMessage());
        }
    }

    public void logout(String hrdNetId, String loginUser) {
        log.info(hrdNetId);
        log.info(loginUser);
        // access token hrdNetId
        if (!hrdNetId.equals(loginUser)) {
            log.info("여기");
            throw new IllegalArgumentException(ExceptionMessage.AUTHENTICATION_FAILED.getMessage());
        }

        studentMapper.removeToken(hrdNetId);
        if (studentMapper.selectRegisteredStudentBasicInfo(hrdNetId) == null) {
            throw new IllegalArgumentException(ExceptionMessage.ILLEGAL_ARGUMENT.getMessage());
        }
    }


}
