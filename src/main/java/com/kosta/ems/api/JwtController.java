package com.kosta.ems.api;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kosta.ems.api.dto.AttendanceHistoryResponse;
import com.kosta.ems.api.dto.UpdateStudentInfoRequest;
import com.kosta.ems.course.CourseDTO;
import com.kosta.ems.student.dto.StudentInfoDTO;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class JwtController {
    private final ApiService service;
    @Value("${security.level}")
    private String SECURITY_LEVEL;

    //과정조회, 포인트조회
    @GetMapping("/course-list")
    @PreAuthorize("hasRole('STUDENT')")

    public Map getAllTakenCourses() {
        StudentInfoDTO loginUser = getLoginUser();
        return Map.of("result", service.getAllTakenCoursesByStudentId(loginUser.getStudentId()));
    }

    //출결조회
    @GetMapping("/attendance-list")
    @PreAuthorize("hasRole('STUDENT')")

    public Map getAttendanceByMonth(LocalDate date) {
        StudentInfoDTO loginUser = getLoginUser();

        double monthAttendanceRate;

        CourseDTO currentCourse = service.getCurrentCourse(loginUser.getStudentId());
        if (currentCourse == null) {
            return Map.of("result", "[]");
        } else {
            List<AttendanceHistoryResponse> attendances = service.getAttendanceByMonth(date, loginUser.getStudentId());
            int fullDaysOfMonth = 0;
            int attendanceDays = 0;
            LocalDate temp = LocalDate.of(date.getYear(), date.getMonth(), 1);
            while (temp.getMonth() == date.getMonth()) {
                if (temp.getDayOfWeek().getValue() <= 5)
                    fullDaysOfMonth++;
                temp = temp.plusDays(1);
            }
            for (AttendanceHistoryResponse item : attendances) {
                if (item.getStatus().equals("출석"))
                    attendanceDays++;
            }
            monthAttendanceRate = (100 * attendanceDays / fullDaysOfMonth);
            return Map.of("result", attendances, "monthAttendanceRate", monthAttendanceRate);
        }

    }

    @GetMapping("/total-attendance-rate")
    @PreAuthorize("hasRole('STUDENT')")

    public Map getTotalAttendanceRate() {
        StudentInfoDTO loginUser = getLoginUser();
        return Map.of("result", service.getTotalAttendanceRate(loginUser.getStudentId()));
    }

    //포인트조회
    @GetMapping("/point-history")
    @PreAuthorize("hasRole('STUDENT')")

    public Map getPointHistory(int courseSeq) {
        StudentInfoDTO loginUser = getLoginUser();
        return Map.of("result", service.getPointHistory(courseSeq, loginUser.getStudentId()));
    }
    
    //마이페이지 정보 조회
    @GetMapping("/student")
    @PreAuthorize("hasRole('STUDENT')")

    public Map getStudentInfo() {
        StudentInfoDTO loginUser = getLoginUser();
        return Map.of("result", loginUser);
    }

    //마이페이지 정보 수정 모달
    @PutMapping("/student")
    @PreAuthorize("hasRole('STUDENT')")
    public Map updateStudentInfo(@RequestBody UpdateStudentInfoRequest dto) {
        StudentInfoDTO loginUser = getLoginUser();
        return Map.of("result", service.updateStudentContactInfo(loginUser.getStudentId(), dto));
    }
    
    //현재 수강중인 과정
    @GetMapping("/current-course")
    @PreAuthorize("hasRole('STUDENT')")
    public Map GetCurrentCourse() {
        StudentInfoDTO loginUser = getLoginUser();
        return Map.of("result", service.getCurrentCourse(loginUser.getStudentId()));
    }
    //마이페이지 입실/
    @GetMapping("/time")
    @PreAuthorize("hasRole('STUDENT')")
    public Map GetAttendanceTimeStatus() {
        StudentInfoDTO loginUser = getLoginUser();
        return Map.of("result", service.getAttendanceTimeStatus(loginUser.getStudentId()));
    }

    @PostMapping("/in-time")
    @PreAuthorize("hasRole('STUDENT')")
    public Map recordInTime() {
        StudentInfoDTO loginUser = getLoginUser();
        return Map.of("result", service.addInTime(loginUser.getStudentId()));
    }

    @PostMapping("/out-time")
    @PreAuthorize("hasRole('STUDENT')")
    public Map recordOutTime() {
        StudentInfoDTO loginUser = getLoginUser();
        return Map.of("result", service.addOutTime(loginUser.getStudentId()));
    }

    private StudentInfoDTO getLoginUser() {
        StudentInfoDTO loginUser;
         loginUser = service.getStudentByStudentCourseSeq(19);

        return loginUser;
    }

}
