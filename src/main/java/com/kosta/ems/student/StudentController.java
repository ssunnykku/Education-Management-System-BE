package com.kosta.ems.student;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.kosta.ems.student.PageRequestDTO;

@RestController
public class StudentController {
	@Autowired
	private StudentServiceImpl studentServiceImpl;
	
	// 수강생 관리 - 수강생 정보 조회
	@GetMapping("/students/student-list/{name}/{courseNumber}")
	// public List<StudentBasicInfoDTO> getStudentsByNameOrCourseNumber(@PathVariable String name, @PathVariable int courseNumber) {
	public Map<String, Object> getStudentsByNameOrCourseNumber(@PathVariable("name") String name, @PathVariable("courseNumber") int courseNumber, @RequestBody PageRequestDTO pageRequest) {
		int page = pageRequest.getPage();
		int size = pageRequest.getSize();
		
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("data", studentServiceImpl.getStudentsByNameOrCourseNumber(name, courseNumber, page, size));
		
		// 페이지네이션
		/*
		 * PageResponseDTO<StudentBasicInfoDTO> list(PageRequestDTO pageRequestDTO) { //
		 * Pageable pageable = Page }
		 */
		
		// Map<String, Collection> result = new HashMap<String, Collection>();
		// result.put("data", studentServiceImpl.getStudentsByNameOrCourseNumber(name, courseNumber));
		// return studentServiceImpl.getStudentsByNameOrCourseNumber(name, courseNumber);
		return result;
	}
}
