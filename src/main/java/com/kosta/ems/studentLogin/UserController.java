package com.kosta.ems.studentLogin;

import com.kosta.ems.config.jwt.JwtTokenProvider;
import com.kosta.ems.config.jwt.TokenInfo;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
@Slf4j
public class UserController {
    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;
    private final static String REFRESH_TOKEN = "refreshToken";


//    로그인시 at, rt -> 재발급 해줌
//    at는 응답으로 보내고 rt는 쿠키에 저장
//    만료시 exception -> 401 -> 쿠키의 rt 확인,, 비교 후 ac 재발급
//

    @PreAuthorize("hasRole('STUDENT')")
    @PostMapping("/students/login")
    public ResponseEntity<Map<String, Object>> studentLogin(@RequestBody StudentDTO studentDTO) {
        TokenInfo result = userService.login(studentDTO);

//        // 쿠키에 refresh token 저장
//        Cookie refreshTokenCookie = new Cookie(REFRESH_TOKEN, result.getRefreshToken());
//        refreshTokenCookie.setHttpOnly(true);
//        refreshTokenCookie.setPath("/");
////        refreshTokenCookie.setSameSite(true);
////        refreshTokenCookie.setSecure(true);
//
//
//        response.addCookie(refreshTokenCookie);

        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("result", result.getAccessToken()));
    }

    @GetMapping("/token")
    public ResponseEntity<Map<String, Object>> createToken(HttpServletRequest request, HttpServletResponse response) {

        String userAccessToken = jwtTokenProvider.getAccessToken(request);
//        log.info("token1{}", userAccessToken);
//        log.info("쿠키 가져와{} ", jwtTokenProvider.getCookieValue(request, "refreshToken"));
//        String Cookie = jwtTokenProvider.getCookieValue(request, "refreshToken");

        TokenInfo result = null;
//        try {
        result = userService.isRefreshTokenValid("refreshToken", userAccessToken);
//            Cookie refreshTokenCookie = new Cookie(REFRESH_TOKEN, result.getRefreshToken());
//            refreshTokenCookie.setHttpOnly(true);
//            refreshTokenCookie.setPath("/");
//
//            response.addCookie(refreshTokenCookie);


//        } catch (ResponseStatusException e) {
//            log.info("1 {}", e.getMessage());
//            log.error(e.getMessage());
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(e.getReason(), e.getMessage()));
//
//        } catch (SecurityException | MalformedJwtException | UnsupportedJwtException | IllegalArgumentException |
//                 ExpiredJwtException e) {
//            try {
//                log.info("2 {}", e.getMessage());
////                TokenInfo token = userService.isRefreshTokenValid(jwtTokenProvider.getCookieValue(request, REFRESH_TOKEN), userAccessToken);
//
////                Cookie refreshTokenCookie = new Cookie(REFRESH_TOKEN, result.getRefreshToken());
////                refreshTokenCookie.setHttpOnly(true);
////                refreshTokenCookie.setPath("/");
////                refreshTokenCookie.setSecure(true);
////
////                response.addHeader("Set-Cookie", String.format("%s=%s; Max-Age=%d; Path=%s; Secure; HttpOnly; SameSite=None",
////                        refreshTokenCookie.getName(),
////                        refreshTokenCookie.getValue(),
////                        refreshTokenCookie.getMaxAge(),
////                        refreshTokenCookie.getPath()));
////
////                response.addCookie(refreshTokenCookie);
//
////                return ResponseEntity.status(HttpStatus.ACCEPTED).body(Map.of("result", token.getRefreshToken()));
//
//            } catch (SecurityException | MalformedJwtException | UnsupportedJwtException | IllegalArgumentException |
//                     ExpiredJwtException error) {
//                log.info("3 {}", error.getMessage());
//                Map<String, Object> errorMessage = new HashMap<>();
//                errorMessage.put("message", error.getMessage());
//                errorMessage.put("status", 401);
//                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("result", errorMessage));
//            }
//
//
//        }
        return ResponseEntity.status(HttpStatus.OK).body(Map.of("result", result.getAccessToken()));
    }

    @GetMapping("/logout")
    public ResponseEntity<Map<String, Object>> logout(@RequestParam String hrdNetId, HttpServletRequest request) {
        try {
//            String loginUser = jwtTokenProvider.getHrdNetId(jwtTokenProvider.getAccessToken(request));
            String loginUser = jwtTokenProvider.getHrdNetId(request);

            userService.logout(hrdNetId, loginUser);

            return ResponseEntity.status(HttpStatus.OK).body(Map.of("result", true));

        } catch (IllegalArgumentException e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
        }
    }


}


