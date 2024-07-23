package com.kosta.ems;

import com.kosta.ems.student.dto.StudentBasicInfoDTO;
import com.kosta.ems.student.StudentService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import com.kosta.ems.attendance.AttendanceService;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import com.kosta.ems.course.CourseDTO;
import com.kosta.ems.course.CourseService;
import com.kosta.ems.manager.ManagerDTO;
import com.kosta.ems.manager.ManagerService;
import com.kosta.ems.notification.NotificationService;
import com.kosta.ems.studentPoint.StudentPointService;
import com.kosta.ems.studentPoint.dto.StudentCourseWithPointDTO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("/ems")
@RequiredArgsConstructor
@Slf4j
public class EmsController {
    @Value("${security.level}")
    private String SECURITY_LEVEL;

    private final CourseService courseService;
    private final StudentPointService pointService;
    private final StudentService studentService;
    @Qualifier("notificationService")
    private final NotificationService notification;
    private final AttendanceService attendanceService;
    private final ManagerService managerService;

    @GetMapping("/benefits")
    @PreAuthorize("hasRole('MANAGER')")
    public String benefitBoard() {
        return "benefits/benefitBoard";
    }

    @GetMapping("/benefits/result")
    @PreAuthorize("hasRole('MANAGER')")

    public String benefitResultBoard() {
        return "benefits/benefitResultBoard";
    }

    @GetMapping("/certifications")
    @PreAuthorize("hasRole('MANAGER')")
    public String certificationBoard() {
        return "certifications/certificationBoard";
    }

    @GetMapping("/courses")
    @PreAuthorize("hasRole('MANAGER')")
    public String addCourseModal(@RequestParam(value = "page", defaultValue = "1") int page,
                                 @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
                                 @RequestParam(value = "courseNumber", defaultValue = "0") int courseNumber,
                                 @RequestParam(value = "excludeExpired", defaultValue = "true") boolean excludeExpired,
                                 Model model
            , Principal principal) {
        ManagerDTO loginUser = getLoginUser();
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
        ManagerDTO loginUser;
        if (SECURITY_LEVEL.equals("OFF")) {
            loginUser = managerService.findByEmployeeNumber("EMP0001");
        } else {
            loginUser = (ManagerDTO) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        }
        return loginUser;
    }

    @GetMapping("/login")
    @Secured("ROLE_MANAGER")
    public String login() {
        return "login/login";
    }

    @GetMapping("/notifications")
    @Secured("ROLE_MANAGER")
    public String notificationBoard(@RequestParam(defaultValue = "1") int page) {
        return "notifications/notificationBoard";
    }

    @GetMapping("/notification")
    @PreAuthorize("hasRole('MANAGER')")
    public String notificationPost(@RequestParam("notificationSeq") int notificationSeq) {
        return "notifications/notification";
    }

    @GetMapping("/notifications/write")
    @PreAuthorize("hasRole('MANAGER')")
    public String notificationWrite() {
        return "notifications/setNotification";
    }

    @GetMapping("/notification/update")
    @PreAuthorize("hasRole('MANAGER')")
    public String notificationSet(@RequestParam("notificationSeq") int notificationSeq) {
        return "notifications/editNotification";
    }

    @GetMapping("/scholarships")
    @PreAuthorize("hasRole('MANAGER')")
    public String scholarshipBoard() {
        return "scholarships/scholarshipBoard";
    }

    @GetMapping("/scholarships/results")
    @PreAuthorize("hasRole('MANAGER')")
    public String scholarshipResultBoard() {
        return "scholarships/scholarshipResultBoard";
    }

    @GetMapping("/attendances/add")
    @PreAuthorize("hasRole('MANAGER')")
    public String addAttendance() {
        return "students/addAttendance";
    }

    @GetMapping("/attendances")
    @PreAuthorize("hasRole('MANAGER')")
    public String attendanceBoard() {
        return "students/attendanceBoard";
    }

    @GetMapping("/points")
    @PreAuthorize("hasRole('MANAGER')")
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
        System.out.println(paging);
        model.addAttribute("studentList", studentList);
        model.addAttribute("pointCategoryList", pointService.getPointCategoryList());

        return "students/pointBoard";
    }

    @GetMapping("/students/update/{selected}")
    @PreAuthorize("hasRole('MANAGER')")
    public String updateStudent(@PathVariable String selected, Model model) {
        StudentBasicInfoDTO dto = studentService.getRegisteredStudentInfo(selected);
        model.addAttribute("dto", dto);
        return "students/setStudent";
    }

    @GetMapping("/students/set")
    @PreAuthorize("hasRole('MANAGER')")
    public String addStudent() {
        return "students/addStudent";
    }

    @GetMapping("/students")
    @PreAuthorize("hasRole('MANAGER')")
    public String studentBoard() {
        return "students/studentBoard";
    }

    @GetMapping("/employments")
    @PreAuthorize("hasRole('MANAGER')")
    public String employmentBoard() {
        return "employment/employmentBoard";
    }

    @GetMapping
    @PreAuthorize("hasRole('MANAGER')")
    public String main() {
        return "main";
    }

    @GetMapping("/employments/data")
    @PreAuthorize("hasRole('MANAGER')")
    public String employment() {
        return "employment/employmentDataBoard";
    }
    

}
