package com.kosta.ems.employment;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kosta.ems.course.CourseDTO;
import com.kosta.ems.course.CourseService;
import com.kosta.ems.employment.dto.AddEmployeedStatusRequest;
import com.kosta.ems.employment.dto.EditEmployeedStatusRequest;
import com.kosta.ems.manager.ManagerDTO;
import com.kosta.ems.manager.ManagerService;
import com.kosta.ems.studentCourse.StudentCourseService;

import jakarta.websocket.server.PathParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.lang.ProcessHandle.Info;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/employments")
@RequiredArgsConstructor
@Slf4j
public class EmploymentController {
    private final EmploymentService service;
    private final StudentCourseService studentCourseService;
    private final ManagerService managerService;
    private final CourseService courseService;
    @Value("${security.level}")
    private String SECURITY_LEVEL;
    
    @GetMapping("/info-list")
    public Map getMethodName(@RequestParam int courseNumber) {
        List<EmploymentInfoResponse> list = service.getEmploymentInfoByCourseNumber(courseNumber);
        return Map.of("result", list, "total", list.size());
//        return Map.of("result", service.getEmploymentInfoByCourseNumber(courseNumber), "total", studentCourseService.countByCourseNumber(courseNumber));
    }
    
    @GetMapping("/avg-rate")
    public Map getCourseAvgRate(@RequestParam int courseNumber) {
        return Map.of("result", service.getEmployeedRatePct(courseNumber));
    }
    
    @GetMapping("/count-employeed")
    public Map getCountEmployeedByCourseNumber(@RequestParam int courseNumber) {
        return Map.of("result", service.countEmployeedByCourseNumber(courseNumber));
    }
    
    @GetMapping("/count-students")
    public Map getCountStudentsByCourseNumber(@RequestParam int courseNumber) {
        ManagerDTO loginUser = getLoginUser();
        return Map.of("result", studentCourseService.countByCourseNumber(courseNumber));
    }
    
    @PutMapping("/student")
    public Map editEmployeedStatus(@RequestBody EditEmployeedStatusRequest request) {
        ManagerDTO loginUser = getLoginUser();
        return Map.of("result", service.editEmployeedStatus(request, loginUser.getManagerId()));
    }
   
    
    private ManagerDTO getLoginUser() {
        ManagerDTO loginUser;
        if(SECURITY_LEVEL.equals("OFF")) {
            loginUser = managerService.findByEmployeeNumber("EMP0001");
        }else {
            loginUser = (ManagerDTO) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        }
        return loginUser;
    }
}
