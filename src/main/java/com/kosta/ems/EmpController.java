package com.kosta.ems;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import com.kosta.ems.attendance.AttendanceService;
import com.kosta.ems.student.PageResponseDTO;
import org.springframework.web.bind.annotation.*;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import com.kosta.ems.course.CourseDTO;
import com.kosta.ems.course.CourseService;
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
	@Autowired
	private NotificationService notification;
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
			HttpServletRequest request, Model model) {
		// ( 277, "가산", 1, 10, true);
		List<CourseDTO> courseList = courseService.searchCourseList(courseNumber, getAcademyOfLoginUser(request), page,
				pageSize, excludeExpired);
		Integer totalCourseCount = courseService.getSearchCourseListSize(courseNumber, getAcademyOfLoginUser(request),
				page, pageSize, excludeExpired);
		List<Integer> courseNumberList = courseService.getCourseNumberList(getAcademyOfLoginUser(request),
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

	private String getAcademyOfLoginUser(HttpServletRequest request) {
		HttpSession session = request.getSession();
//    	return (String) session.getAttribute("academyLocation");
		return "가산";
	}


	@GetMapping("/login")
	public String login() {
		return "login/login";
	}

	 @GetMapping("/notifications") //@AuthenticationPrincipal 사용할수있음.
    public String notificationBoard(@RequestParam(defaultValue = "1") int page) {
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

	@GetMapping("/notification/update")
	public String notificationSet(@RequestParam("notificationSeq") int notificationSeq) {
		return "notifications/editNotification";
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
	public String attendanceBoard(@RequestParam(value="courseNumber", defaultValue = "-1", required = false) int courseNumber, String name, @RequestParam(value="page", defaultValue = "1") int page, Model model) {
		// int page = 0;
		int size = 10;
		int totalCount = 0;

		if(name == null) {
			name = "none";
		}

		if(name != "none" && courseNumber != -1) {
			// 기수, 수강생명 모두 입력해 검색
			// totalCount = attendanceServiceImpl.getAttendanceIntegratedListFilterAllAmount(name, courseNumber);
			System.out.println(">> if");
			System.out.println(">> name: " + name);
			System.out.println(">> courseNumber: " + courseNumber);
			totalCount = attendanceService.getAttendanceIntegratedListFilterAllAmount(name, courseNumber);
			// 수강생 출결 목록 데이터
			// result.put("data", attendanceServiceImpl.getAttendanceIntegratedListFilterAll(name, courseNumber, page, size));
			model.addAttribute("amount", totalCount);
			model.addAttribute("attendanceList", attendanceService.getAttendanceIntegratedListFilterAll(name, courseNumber, page, size));
			model.addAttribute("searchCourseNumber", courseNumber);
			model.addAttribute("searchStudentName", name);
		} else if(name == "none" && courseNumber == -1) {
			// 기수, 수강생명 모두 미입력 검색 (전체 데이터 + 페이지네이션)
			System.out.println(">> else if");
			System.out.println(">> name: " + name);
			System.out.println(">> courseNumber: " + courseNumber);
			totalCount = attendanceService.getAttendanceIntegratedListNoFilterAmount(name, courseNumber);
			model.addAttribute("amount", totalCount);
			model.addAttribute("attendanceList", attendanceService.getAttendanceIntegratedListNoFilter(name, courseNumber, page, size));
			model.addAttribute("searchCourseNumber", courseNumber);
			model.addAttribute("searchStudentName", name);
		} else {
			// 기수 또는 수강생명 입력하여 검색
			// totalCount = attendanceServiceImpl.getAttendanceIntegratedListFilterAmount(name, courseNumber);
			System.out.println(">> else");
			System.out.println(">> name: " + name);
			System.out.println(">> courseNumber: " + courseNumber);
			totalCount = attendanceService.getAttendanceIntegratedListFilterAmount(name, courseNumber);
			// 수강생 출결 목록 데이터
			// result.put("data", attendanceServiceImpl.getAttendanceIntegratedListFilter(name, courseNumber, page, size));
			model.addAttribute("amount", totalCount);
			model.addAttribute("attendanceList", attendanceService.getAttendanceIntegratedListFilter(name, courseNumber, page, size));
			model.addAttribute("searchCourseNumber", courseNumber);
			model.addAttribute("searchStudentName", name);
		}

		// 페이징 response
		// int totalCount = attendanceServiceImpl.getStudentAttendanceListAmount(name, courseNumber);
		int totalPage = (totalCount/size) + 1;
		// int currentPage = pageRequest.getCurrentPage();
		int currentPage = 1;  // 브라우저에서 받아올 값인데 아직 연결안해서 controller 테스트를 위해 작성했던 코드.
		int prevPage = 0;
		int nextPage = 0;
		if(currentPage > 1 && currentPage < totalPage) {
			prevPage = currentPage - 1;
			nextPage = currentPage + 1;
		} else if(currentPage == totalPage) {
			prevPage = currentPage - 1;
		} else if(currentPage == 1) {
			nextPage = currentPage + 1;
		}

		PageResponseDTO pageInfo = PageResponseDTO.builder().totalCount(totalCount).totalPage(totalPage).currentPage(currentPage).prevPage(prevPage).nextPage(nextPage).build();
		// result.put("pageInfo", pageInfo);
		model.addAttribute("pageInfo", pageInfo);

		return "students/attendanceBoard";
	}

	@GetMapping("/points")
	public String pointBoard(@RequestParam(value = "page", defaultValue = "1") int page,
			@RequestParam(value = "pageSize", defaultValue = "10")             int pageSize,
			@RequestParam(value = "courseNumber", defaultValue = "0")          int courseNumber,
																			   String studentName,
			@RequestParam(value = "excludeExpired", defaultValue = "true") 	   boolean excludeExpired,
			HttpServletRequest request, Model model) {
		List<StudentCourseWithPointDTO> studentList = pointService.getStudentListWithPoint(courseNumber, studentName, page, pageSize, getAcademyOfLoginUser(request));
		Integer totalStudentCount = pointService.getCountOfStudentWithPoint(courseNumber, studentName, getAcademyOfLoginUser(request));
		List<Integer> courseNumberList = courseService.getCourseNumberList(getAcademyOfLoginUser(request),
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
