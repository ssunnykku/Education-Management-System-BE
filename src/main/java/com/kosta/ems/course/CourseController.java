package com.kosta.ems.course;

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

import com.kosta.ems.course.dto.AddCourseRequest;
import com.kosta.ems.manager.ManagerDTO;
import com.kosta.ems.manager.ManagerService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/courses")
@RequiredArgsConstructor
public class CourseController {
    private final CourseService courseService;
    private final ManagerService managerService;
    @Value("${security.level}")
    private String SECURITY_LEVEL;

	@GetMapping("/course")
	public Map<String, CourseDTO> getCourse(@RequestParam int courseSeq) {
	    ManagerDTO loginUser = getLoginUser();
		return Map.of("result", courseService.getCourse(courseSeq, loginUser.getAcademyLocation()));
	}

	@GetMapping("/course-list")
	public Map CourseList(
			@RequestParam(value = "page",           defaultValue = "1"   ) int page,
			@RequestParam(value = "pageSize",       defaultValue = "10"  ) int pageSize,
			@RequestParam(value = "courseNumber",   defaultValue = "0"   ) int courseNumber,
			@RequestParam(value = "excludeExpired", defaultValue = "true") boolean excludeExpired,
			HttpServletRequest request)
	{
	    ManagerDTO loginUser = getLoginUser();
		String academyLocation = loginUser.getAcademyLocation();
		return Map.of("result", courseService.searchCourseList(courseNumber, academyLocation, page, pageSize, excludeExpired));
	}
	
	@GetMapping("/course-number-list")
	public Map getCourseNumberList(@RequestParam(value="excludeExpired", defaultValue = "true") boolean excludeExpired, HttpServletRequest request) {
	    ManagerDTO loginUser = getLoginUser();
		return Map.of("result", courseService.getCourseNumberList(loginUser.getAcademyLocation(), excludeExpired));
	}
	@GetMapping("/course-type-list")
	public Map getCourseTypeList() {
		return Map.of("result", courseService.getCourseTypeList());
	}

	@GetMapping("/course-list-by-year")
	public Map getCourseNumberByYear(@RequestParam int courseEndYear) {
		return Map.of("result", courseService.getCourseNumberByYear(courseEndYear));
	}
	

	@GetMapping("/course-year-list")
	public Map getCourseNumberYearList() {
		return Map.of("result",courseService.getCourseNumberYearList());
	}

	@PostMapping("/course")
	public Map<String, Boolean> addCourse(@RequestBody @Valid AddCourseRequest cRequest, BindingResult bindingResult){
	    if(bindingResult.hasErrors()) {
	        return Map.of("result",false);
	    }
		ManagerDTO loginUser = getLoginUser();
		CourseDTO course = cRequest.toCourseDTO();
		course.setManagerId(loginUser.getManagerId());
		course.setAcademyLocation(loginUser.getAcademyLocation());
		boolean result = courseService.addCourse(course);
		return Map.of("result", result);
	}

	@PutMapping("/course")
	public Map<String, Boolean> editCourse(@RequestBody CourseDTO course){
	    ManagerDTO loginUser = getLoginUser();
		course.setManagerId(loginUser.getManagerId());
		course.setAcademyLocation(loginUser.getAcademyLocation());
		boolean result = courseService.editCourse(course);
		return Map.of("result", result);
	}

	@PatchMapping("/course/{courseSeq}")
	public Map<String, Boolean> deleteCourse(@PathVariable("courseSeq") int courseSeq){
	    ManagerDTO loginUser = getLoginUser();
		boolean result = courseService.deleteCourse(courseSeq, loginUser.getAcademyLocation());
		return Map.of("result", result);
	}

	@GetMapping("/students-number")
	public Map getStudentsNumberBySeq(@RequestParam int courseNumber) {
		int courseSeq=courseService.getSeqByCourseNumber(courseNumber);
		return Map.of("result", courseService.getStudentsNumberBySeq(courseSeq));
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

    @GetMapping("/current-list")
    public Map<String, List<CourseDTO>> currentProcessList(@RequestParam LocalDate currentDate) {
        ManagerDTO loginUser = getLoginUser();

        return Map.of("result", courseService.getCurrentCourseList(currentDate, loginUser.getAcademyLocation()));
    }
    @GetMapping("/course-name")
    public Map getCourseNameByCourseNumber(@RequestParam int courseNumber) {
    	return Map.of("result",courseService.getCourseNameByCourseNumber(courseNumber));
    }
    
    

}
