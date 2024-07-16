package com.kosta.ems.config.jwt;

import com.kosta.ems.student.StudentDTO;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
@Slf4j
public class UserController {
    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("/students/login")
    public ResponseEntity<Map<String, TokenInfo>> studentLogin(@RequestBody StudentDTO studentDTO) {
        TokenInfo result = userService.login(studentDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("result", result));
    }

    @PostMapping("/token")
    public ResponseEntity<Map<String, Object>> createToken(@RequestBody TokenInfo tokenInfo) {
        TokenInfo result = null;
        try {
            result = userService.isRefreshTokenValid(tokenInfo.getRefreshToken());
        } catch (ResponseStatusException e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(e.getReason(), e.getMessage()));
        } catch (SecurityException | MalformedJwtException | UnsupportedJwtException | IllegalArgumentException |
                 ExpiredJwtException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));

        }
        return ResponseEntity.status(HttpStatus.OK).body(Map.of("result", result));
    }

    @GetMapping("/logout")
    public ResponseEntity<Map<String, Object>> logout(@RequestParam String hrdNetId, HttpServletRequest request) {
        try {
            String loginUser = jwtTokenProvider.getHrdNetId(request);

            userService.logout(hrdNetId, loginUser);
            
            return ResponseEntity.status(HttpStatus.OK).body(Map.of("result", true));

        } catch (IllegalArgumentException e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
        }
    }
}


