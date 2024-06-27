package com.kosta.ems.course;

import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/courses")
@RequiredArgsConstructor
public class CourseController {
	private final CourseService service;
	
	@GetMapping("/course")
	public Map<String, CourseDTO> getCourse(@RequestParam int courseSeq, HttpServletRequest request) {
		return Map.of("result", service.getCourse(courseSeq, getAcademyOfLoginUser(request)));
	}
	
	@GetMapping("/course-number-list")
	public Map getCourseNumberList(@RequestParam(value="excludeExpired", defaultValue = "true") boolean excludeExpired, HttpServletRequest request) {
		return Map.of("result", service.getCourseNumberList(getAcademyOfLoginUser(request), excludeExpired));
	}
	@GetMapping("/course-type-list")
	public Map getCourseTypeList() {
		return Map.of("result", service.getCourseTypeList());
	}
	
	
	@PostMapping("/course")
	public Map<String, Boolean> addCourse(@RequestBody CourseDTO course, HttpServletRequest request){
		course.setManagerId(getManagerIdOfLoginUser(request));
		course.setAcademyLocation(getAcademyOfLoginUser(request));
		boolean result = service.addCourse(course);
		return Map.of("result", result);
	}
	
	@PutMapping("/course")
	public Map<String, Boolean> editCourse(@RequestBody CourseDTO course, HttpServletRequest request){
		course.setManagerId(getManagerIdOfLoginUser(request));
		course.setAcademyLocation(getAcademyOfLoginUser(request));
		boolean result = service.editCourse(course);
		return Map.of("result", result);
	}
	
	@PatchMapping("/course/{courseSeq}")
	public Map<String, Boolean> deleteCourse(@PathVariable("courseSeq") int courseSeq, HttpServletRequest request){
		boolean result = service.deleteCourse(courseSeq, getAcademyOfLoginUser(request));
		return Map.of("result", result);
	}

	private String getAcademyOfLoginUser(HttpServletRequest request) {
		HttpSession session = request.getSession();
		return "가산";
//		return (String) session.getAttribute("academyLocation");
	}
	
	private String getManagerIdOfLoginUser(HttpServletRequest request) {
		HttpSession session = request.getSession();
		return "d893bf71-2f8f-11ef-b0b2-0206f94be675";
//		return (String) session.getAttribute("managerId");
	}
	
	
	
}
