package com.kosta.ems.studentPoint;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.kosta.ems.student.StudentService;
import com.kosta.ems.studentPoint.dto.PointCategoryDTO;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/points")
@RequiredArgsConstructor
@Slf4j
public class StudentPointController {
	private final StudentPointService service;
	private final StudentService studnetService;
	
	@GetMapping("/category-list")
	public Map<String, List<PointCategoryDTO>> pointCategoryList(){
		return Map.of("data", service.getPointCategoryList());
	}
	
}
