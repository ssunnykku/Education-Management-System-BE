package com.kosta.ems.employment;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kosta.ems.course.CourseService;
import com.kosta.ems.studentCourse.StudentCourseService;

import lombok.RequiredArgsConstructor;

import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/employments")
@RequiredArgsConstructor
public class EmploymentController {
    private final EmploymentService service;
    private final StudentCourseService studentCourseService;
    
    @GetMapping("/infoList")
    public Map getMethodName(@RequestParam(value = "page",           defaultValue = "1"   ) int page,
            @RequestParam(value = "pageSize",       defaultValue = "10"  ) int pageSize,
            @RequestParam int courseSeq) {
        return Map.of("result", service.getEmploymentInfoByCourseSeq(courseSeq, page, pageSize), "total", studentCourseService.countByCourseSeq(courseSeq));
    }
    
}
