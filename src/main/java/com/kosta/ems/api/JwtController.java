package com.kosta.ems.api;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kosta.ems.api.dto.TakenCourseResponse;
import com.kosta.ems.course.CourseService;
import com.kosta.ems.course.dto.AddCourseRequest;
import com.kosta.ems.manager.ManagerDTO;
import com.kosta.ems.manager.ManagerService;
import com.kosta.ems.student.StudentDTO;
import com.kosta.ems.student.StudentInfoDTO;
import com.kosta.ems.student.StudentService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/ems")
@RequiredArgsConstructor
public class JwtController {
	private final ApiService service;
    @Value("${security.level}")
    private String SECURITY_LEVEL;
	
    private List<TakenCourseResponse> getAllTakenCourses(){
        StudentInfoDTO loginUser = getLoginUser();
        List<TakenCourseResponse> result = null;
        
        result = service.getAllTakenCoursesByStudentId(loginUser.getStudentId());
        
        return result;
    }
    
    private StudentInfoDTO getLoginUser() {
        StudentInfoDTO loginUser;
        loginUser = StudentInfoDTO.builder()
                .studentId("738003dc-3eb0-11ef-bd30-0206f94be675")
                .hrdNetId("syc1234")
                .name("손유철")
                .birth(LocalDate.of(2002, 2, 16))
                .address("경기도 부천시 소사로 111 연꽃가득아파트 101호")
                .bank("국민")
                .account("110583195038")
                .phoneNumber("01059341921")
                .email("syc1234@gmail.com")
                .gender('M')
                .isActive(true)
                .build();
//        if (SECURITY_LEVEL.equals("OFF")) {
//        }
//        else {
//            loginUser = (StudentDTO) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        }
        return loginUser;
    }
	
}
