package com.kosta.ems.config.jwt;

import com.kosta.ems.student.StudentDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
@Slf4j
public class UserController {
    private final UserService userService;

    @PostMapping("/students/login")
    public ResponseEntity<Map<String, TokenInfo>> studentLogin(@RequestBody StudentDTO studentDTO) {
        TokenInfo result = userService.login(studentDTO);
        log.info("로그인 결과 {} ", result);
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("result", result));
    }
}
