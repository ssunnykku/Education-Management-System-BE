package com.kosta.ems.config.jwt;

import com.kosta.ems.student.StudentDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RequiredArgsConstructor
@RestController
@Slf4j
public class userController {
    private final UserService userService;

    @PostMapping("/api/students/login")
    public ResponseEntity<Map<String, TokenInfo>> studentLogin(@RequestBody StudentDTO studentDTO) {
        TokenInfo result = userService.login(studentDTO.getHrdNetId(), studentDTO.getPassword());
        log.info("이거 {} ", result);
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("result", result));

//        return ResponseEntity.ok(Map.of("result", result));
    }
}
