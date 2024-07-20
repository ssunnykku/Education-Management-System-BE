package com.kosta.ems.manager;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/managers")
@RequiredArgsConstructor
@Slf4j
public class ManagerController {
    @Value("${security.level}")
    private String SECURITY_LEVEL;
    private final ManagerService managerService;

    @GetMapping
    public Map currentUser() {
        ManagerDTO loginUser = getLoginUser();
        String managerId = loginUser.getManagerId();

        return Map.of("result", managerService.fintByManagerId(managerId));
    }

    private ManagerDTO getLoginUser() {
        ManagerDTO loginUser;
        if (SECURITY_LEVEL.equals("OFF")) {
            loginUser = managerService.findByEmployeeNumber("EMP0001");
        } else {
            loginUser = (ManagerDTO) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        }
        return loginUser;
    }

}

