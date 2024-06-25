package com.kosta.ems.courses;

import java.util.Map;

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
	
	@PostMapping("/course/course")
	public Map<String, Boolean> addCourse(@RequestBody CourseDTO course, HttpServletRequest request){
		HttpSession session = request.getSession();
		boolean result = service.editCourse(course, (String) session.getAttribute("academyLocation"));
		return Map.of("result", result);
	}
	
	@PutMapping("/course/course")
	public Map<String, Boolean> editCourse(@RequestBody CourseDTO course, HttpServletRequest request){
		HttpSession session = request.getSession();
		boolean result = service.editCourse(course, (String) session.getAttribute("academyLocation"));
		return Map.of("result", result);
	}
	
	@PatchMapping("/course/{courseSeq}")
	public Map<String, Boolean> deleteCourse(@PathVariable("courseSeq") int courseSeq, HttpServletRequest request){
		HttpSession session = request.getSession();
		boolean result = service.deleteCourse(courseSeq, "가산");
		return Map.of("result", result);
	}
	
	
	
}
