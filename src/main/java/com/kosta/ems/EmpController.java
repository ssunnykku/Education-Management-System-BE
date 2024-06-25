package com.kosta.ems;

import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.kosta.ems.courses.CourseDTO;
import com.kosta.ems.courses.CourseService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/ui")
@RequiredArgsConstructor
public class EmpController {
	private final CourseService courseService;

    @GetMapping("/benefits")
    public String benefitBoard() {
        return "benefits/benefitBoard";
    }

    @GetMapping("/benefits/result")
    public String benefitResultBoard() {
        return "benefits/benefitResultBoard";
    }

    @GetMapping("/certifications")
    public String certificationBoard() {
        return "certifications/certificationBoard";
    }

    @GetMapping("/courses")
    public String addCourseModal(@RequestParam(value="page", defaultValue = "1") int page, @RequestParam(value="pageSize", defaultValue = "10") int pageSize, @RequestParam(value="courseNumber", defaultValue = "0") int courseNumber,@RequestParam(value="excludeExpired", defaultValue = "true") boolean excludeExpired, HttpServletRequest request, Model model) {
    	//                                                         (         277,                          "가산",    1,       10);
    	List<CourseDTO> courseList = courseService.searchCourseList(courseNumber, getAcademyOfLoginUser(request), page, pageSize, excludeExpired);
    	List<Integer> courseNumberList = courseService.getCourseNumberList(getAcademyOfLoginUser(request), excludeExpired);
    	model.addAttribute("courseNumberList",courseNumberList);
    	model.addAttribute("courseList",courseList);
        return "courses/courseBoard";
    }

	private String getAcademyOfLoginUser(HttpServletRequest request) {
		HttpSession session = request.getSession();
//    	return (String) session.getAttribute("academyLocation");
		return "가산";
	}

    @GetMapping("/login")
    public String login() {
        return "login/login";
    }

    @GetMapping("/notifications")
    public String notificationBoard() {
        return "notifications/notificationBoard";
    }

    @GetMapping("/notifications/post")
    public String notificationPost() {
        return "notifications/notificationPost";
    }

    @GetMapping("/scholarships")
    public String scholarshipBoard() {
        return "scholarships/scholarshipBoard";
    }

    @GetMapping("/scholarships/results")
    public String scholarshipResultBoard() {
        return "scholarships/scholarshipResultBoard";
    }

    // 여기부터
    @GetMapping("/attendances/add")
    public String addAttendance() {
        return "students/addAttendance";
    }

    @GetMapping("/attendances")
    public String attendanceBoard() {
        return "students/attendanceBoard";
    }

    @GetMapping("/points")
    public String pointBoard() {
        return "students/pointBoard";
    }

    @GetMapping("/students/set")
    public String setStudent() {
        return "students/setStudent";
    }

    @GetMapping("/students")
    public String studentBoard() {
        return "students/studentBoard";
    }


    /*templete*/
    @GetMapping("header")
    public String header() {
        return "common/header";
    }

    /**/
    @GetMapping("/data/add")
    public String data2() {
        return "courses/addCourseModal";
    }
}
