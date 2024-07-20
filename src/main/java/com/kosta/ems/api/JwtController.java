package com.kosta.ems.api;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
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
    public Map getAllTakenCourses() {
        StudentInfoDTO loginUser = getLoginUser();
        return Map.of("result", service.getAllTakenCoursesByStudentId(loginUser.getStudentId()));
    }

    //출결조회
    @GetMapping("/attendance-list")
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
    public Map getTotalAttendanceRate() {
        StudentInfoDTO loginUser = getLoginUser();
        return Map.of("result", service.getTotalAttendanceRate(loginUser.getStudentId()));
    }

    //포인트조회
    @GetMapping("/point-history")
    public Map getPointHistory(int courseSeq) {
        StudentInfoDTO loginUser = getLoginUser();
        return Map.of("result", service.getPointHistory(courseSeq, loginUser.getStudentId()));
    }

    //마이페이지 정보 수정 모달
    @PutMapping("/student")
    public Map updateStudentInfo(@RequestBody UpdateStudentInfoRequest dto) {
        StudentInfoDTO loginUser = getLoginUser();
        return Map.of("result", service.updateStudentContactInfo(loginUser.getStudentId(), dto));
    }

    //마이페이지 입실/
    @GetMapping("/time")
    public Map GetAttendanceTimeStatus() {
        StudentInfoDTO loginUser = getLoginUser();
        return Map.of("result", service.getAttendanceTimeStatus(loginUser.getStudentId()));
    }

    @PostMapping("/in-time")
    public Map recordInTime() {
        StudentInfoDTO loginUser = getLoginUser();
        return Map.of("result", service.addInTime(loginUser.getStudentId()));
    }

    @PostMapping("/out-time")
    public Map recordOutTime() {
        StudentInfoDTO loginUser = getLoginUser();
        return Map.of("result", service.addOutTime(loginUser.getStudentId()));
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
                .isActive('T')
                .build();
//        if (SECURITY_LEVEL.equals("OFF")) {
//        }
//        else {
//            loginUser = (StudentDTO) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        }
        return loginUser;
    }

}
