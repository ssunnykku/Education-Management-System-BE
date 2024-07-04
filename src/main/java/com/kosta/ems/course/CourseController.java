package com.kosta.ems.course;

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

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/courses")
@RequiredArgsConstructor
public class CourseController {
	private final CourseService courseService;
    @Value("${security.level}")
    private String SECURITY_LEVEL;
	
	@GetMapping("/course")
	public Map<String, CourseDTO> getCourse(@RequestParam int courseSeq) {
		return Map.of("result", courseService.getCourse(courseSeq, getAcademyOfLoginUser()));
	}
	
	@GetMapping("/course-list")
	public Map CourseList(
			@RequestParam(value = "page",           defaultValue = "1"   ) int page,
			@RequestParam(value = "pageSize",       defaultValue = "10"  ) int pageSize,
			@RequestParam(value = "courseNumber",   defaultValue = "0"   ) int courseNumber,
			@RequestParam(value = "excludeExpired", defaultValue = "true") boolean excludeExpired, 
			HttpServletRequest request) 
	{
		String academyLocation = getAcademyOfLoginUser();
		return Map.of("result", courseService.searchCourseList(courseNumber, academyLocation, page, pageSize, excludeExpired));
	}
	
	@GetMapping("/course-number-list")
	public Map getCourseNumberList(@RequestParam(value="excludeExpired", defaultValue = "true") boolean excludeExpired, HttpServletRequest request) {
		return Map.of("result", courseService.getCourseNumberList(getAcademyOfLoginUser(), excludeExpired));
	}
	@GetMapping("/course-type-list")
	public Map getCourseTypeList() {
		return Map.of("result", courseService.getCourseTypeList());
	}
	
	
	@PostMapping("/course")
	public Map<String, Boolean> addCourse(@RequestBody @Valid AddCourseRequest cRequest, BindingResult bindingResult){
	    if(bindingResult.hasErrors()) {
	        return Map.of("result",false);
	    }
		ManagerDTO loginUser = getLoginUser();
		CourseDTO course = cRequest.toCourseDTO(loginUser.getAcademyLocation());
		boolean result = courseService.addCourse(course);
		return Map.of("result", result);
	}
	
	@PutMapping("/course")
	public Map<String, Boolean> editCourse(@RequestBody CourseDTO course){
		course.setManagerId(getManagerIdOfLoginUser());
		course.setAcademyLocation(getAcademyOfLoginUser());
		boolean result = courseService.editCourse(course);
		return Map.of("result", result);
	}
	
	@PatchMapping("/course/{courseSeq}")
	public Map<String, Boolean> deleteCourse(@PathVariable("courseSeq") int courseSeq){
		boolean result = courseService.deleteCourse(courseSeq, getAcademyOfLoginUser());
		return Map.of("result", result);
	}

	private String getAcademyOfLoginUser() {
        if(SECURITY_LEVEL.equals("OFF")) {
            return("가산");
        }
        ManagerDTO loginUser = getLoginUser();
        return loginUser.getAcademyLocation();
	}
	
	private String getManagerIdOfLoginUser() {
	    if(SECURITY_LEVEL.equals("OFF")) {
	        return("bd8c73e1-39c9-11ef-aad4-06a5a7b26ae5");
	    }
	    ManagerDTO loginUser = getLoginUser();
		return loginUser.getManagerId();
	}
	
	private ManagerDTO getLoginUser() {
        ManagerDTO loginUser = (ManagerDTO) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return loginUser;
    }
	
}
