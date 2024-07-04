package com.kosta.ems;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import com.kosta.ems.attendance.AttendanceService;
import com.kosta.ems.student.PageResponseDTO;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import com.kosta.ems.course.CourseDTO;
import com.kosta.ems.course.CourseService;
import com.kosta.ems.manager.ManagerDTO;
import com.kosta.ems.notification.NotificationService;
import com.kosta.ems.studentPoint.StudentPointService;
import com.kosta.ems.studentPoint.dto.StudentCourseWithPointDTO;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("/ems")
@RequiredArgsConstructor
@Slf4j
public class EmpController {
    private final CourseService courseService;
    private final StudentPointService pointService;
    @Qualifier("notificationService")
    private final NotificationService notification;
    private final AttendanceService attendanceService;

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
    public String addCourseModal(@RequestParam(value = "page", defaultValue = "1") int page,
                                 @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
                                 @RequestParam(value = "courseNumber", defaultValue = "0") int courseNumber,
                                 @RequestParam(value = "excludeExpired", defaultValue = "true") boolean excludeExpired,
                                 Model model
                                 , Principal principal) {
        ManagerDTO loginUser = getLoginUser();
        // ( 277, "가산", 1, 10, true);
        List<CourseDTO> courseList = courseService.searchCourseList(courseNumber, loginUser.getAcademyLocation(), page,
                pageSize, excludeExpired);
        Integer totalCourseCount = courseService.getSearchCourseListSize(courseNumber, loginUser.getAcademyLocation(),
                page, pageSize, excludeExpired);
        List<Integer> courseNumberList = courseService.getCourseNumberList(loginUser.getAcademyLocation(),
                excludeExpired);

        Map<String, Integer> paging = new HashMap<>();
        paging.put("totalCourseCount", totalCourseCount);
        paging.put("page", page);
        paging.put("pageSize", pageSize);
        paging.put("pageOffset", (((page - 1) / 10) * 10) + 1);// 현재 페이지가 27이라면 offset은 21을 가리킨다.
        paging.put("excludeExpired", excludeExpired ? 1 : 0);

        model.addAttribute("selectedCourseNumber", courseNumber);
        model.addAttribute("courseNumberList", courseNumberList);
        model.addAttribute("paging", paging);
        model.addAttribute("courseList", courseList);
        model.addAttribute("courseTypeList", courseService.getCourseTypeList());

        return "courses/courseBoard";
    }
    
    private ManagerDTO getLoginUser() {
        ManagerDTO loginUser = (ManagerDTO) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return loginUser;
    }

    @GetMapping("/login")
    public String login() {
        return "login/login";
    }

    @GetMapping("/notifications") //@AuthenticationPrincipal 사용할수있음.
    public String notificationBoard(Model model, HttpSession session, @RequestParam(defaultValue = "1") int page) {
        log.info((String) session.getAttribute("managerId").toString());
        //String managerId="d893bf71-2f8f-11ef-b0b2-0206f94be675";

        String managerId = (String) session.getAttribute("managerId");
        model.addAttribute("notification", notification.searchAll(managerId, page, 10));
        log.info(model.addAttribute("notification", notification.searchAll(managerId, page, 10)).toString());
        return "notifications/notificationBoard";
    }


    @GetMapping("/notification")
    public String notificationPost(@RequestParam("notificationSeq") int notificationSeq) {
        return "notifications/notification";
    }

    @GetMapping("/notifications/write")
    public String notificationWrite() {
        return "notifications/setNotification";
    }

    @GetMapping("/notifications/update")
    public String notificationSet() {
        return "notifications/setNotification";
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

    /*
    @GetMapping("/attendances")
    public String attendanceBoard() {
        return "attendances/attendanceBoard";
        // return "students/attendanceBoard";
    }
    */
    @GetMapping("/attendances")
    public String attendanceBoard() {
        return "students/attendanceBoard";
    }

    @GetMapping("/points")
    public String pointBoard(@RequestParam(value = "page", defaultValue = "1") int page,
                             @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
                             @RequestParam(value = "courseNumber", defaultValue = "0") int courseNumber,
                             String studentName,
                             @RequestParam(value = "excludeExpired", defaultValue = "true") boolean excludeExpired,
                             Model model) {
        ManagerDTO loginUser = getLoginUser();
        List<StudentCourseWithPointDTO> studentList = pointService.getStudentListWithPoint(courseNumber, studentName, page, pageSize, loginUser.getAcademyLocation());
        Integer totalStudentCount = pointService.getCountOfStudentWithPoint(courseNumber, studentName, loginUser.getAcademyLocation());
        List<Integer> courseNumberList = courseService.getCourseNumberList(loginUser.getAcademyLocation(),
                excludeExpired);

        Map<String, Integer> paging = new HashMap<>();
        paging.put("totalStudentCount", totalStudentCount);
        paging.put("page", page);
        paging.put("pageSize", pageSize);
        paging.put("pageOffset", (((page - 1) / pageSize) * pageSize) + 1);// 현재 페이지가 27이라면 offset은 21을 가리킨다.
        paging.put("excludeExpired", excludeExpired ? 1 : 0);

        model.addAttribute("searchedStudentName", studentName);
        model.addAttribute("selectedCourseNumber", courseNumber);
        model.addAttribute("courseNumberList", courseNumberList);
        model.addAttribute("paging", paging);
        model.addAttribute("studentList", studentList);
        model.addAttribute("pointCategoryList", pointService.getPointCategoryList());

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

    /* templete */
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
