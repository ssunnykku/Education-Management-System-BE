package com.kosta.ems;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/ui")

public class ErpController {

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
    public String addCourseModal() {
        return "courses/courseBoard";
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
