package com.kosta.ems.student;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import lombok.extern.log4j.Log4j2;

@Log4j2
@RestController
public class StudentController {
	@Autowired
	private StudentServiceImpl studentServiceImpl;
	
	// [수강생 정보] - 수강생 정보 조회  _POSTMAN 확인 완료
	@GetMapping("/students/student-list/{name}/{courseNumber}")
	// public List<StudentBasicInfoDTO> getStudentsByNameOrCourseNumber(@PathVariable String name, @PathVariable int courseNumber) {
	public Map<String, Object> getStudentsByNameOrCourseNumber(@PathVariable("name") String name, @PathVariable("courseNumber") int courseNumber, @RequestBody PageRequestDTO pageRequest) {
		Map<String, Object> result = new HashMap<String, Object>();
		int page = pageRequest.getPage();
		int size = pageRequest.getSize();
		
		// 페이징 response
		int totalCount = studentServiceImpl.getStudentsByNameOrCourseNumberAmount(name, courseNumber);
		int totalPage = (totalCount/size) + 1;
		int currentPage = pageRequest.getCurrentPage();
		// int currentPage = 2;  // 브라우저에서 받아올 값인데 아직 연결안해서 controller 테스트를 위해 작성했던 코드.
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
		result.put("pageInfo", pageInfo);
		
		// 수강생 정보 데이터
		result.put("data", studentServiceImpl.getStudentsByNameOrCourseNumberList(name, courseNumber, page, size));
		
		return result;
	}

	// [수강생 정보] - 수강생 등록
	// 1. 등록된 hrdNetId인지 확인 _POSTMAN 확인 완료 -- 비동기
	@GetMapping("/students/valid-id")
	public Map<String, Object> findByHrdNetId(@RequestBody IsRegisteredStudentDTO request) {
		Map<String, Object> result = new HashMap<String, Object>();
		String hrdNetId = request.getHrdNetId();
		
		boolean check = studentServiceImpl.findByHrdNetId(request.getHrdNetId());
		
		if(check) {
			result.put("result", String.valueOf(check));
		} else {
			// 등록된 수강생 기본 정보 불러오기
			result.put("result", String.valueOf(check));
			RegisteredStudentInfoDTO dto = studentServiceImpl.getRegisteredStudentBasicInfo(hrdNetId);
			result.put("data", dto);
		}
		return result;
	}

	// [수강생 정보] - 수강생 등록
	// 1-1. 신규 수강생 등록
	@PostMapping("/students")
	public UpdateDeleteResultDTO setStudentWithCourse(@RequestBody RequestAddStudentBasicInfoDTO request) {
		UpdateDeleteResultDTO dto = new UpdateDeleteResultDTO();
		try {
			studentServiceImpl.setStudentWithCourse(request.getHrdNetId(), request.getName(), request.getBirth(), request.getAddress(), request.getBank(), request.getAccount(), request.getPhoneNumber(), request.getEmail(), request.getGender(), request.getManagerId(), request.getCourseNumber());
		} catch (NoSuchDataException e) {
			dto.setCode(ResCode.FAIL.value());
			dto.setMessage("Fail: setStudentWithCourse");
		} catch (Exception e) {
			log.error("[StudentController addStudentWithCourse]", e);
		}
		return dto;
	}
	
	// [수강생 정보] - 수강생 등록
	// 1-2. 기존 수강생의 과정 수강 신규 등록
	@PostMapping("/students/new-course")
	public UpdateDeleteResultDTO setRegisteredStudentWithNewCourse(@RequestBody RequestAddStudentBasicInfoDTO request) {
		UpdateDeleteResultDTO dto = new UpdateDeleteResultDTO();
		try {
			studentServiceImpl.setStudentCourseSeqInfo(request.getHrdNetId(), request.getCourseNumber());
		} catch (NoSuchDataException e) {
			dto.setCode(ResCode.FAIL.value());
			dto.setMessage("Fail: setRegisteredStudentWithNewCourse");
		} catch (Exception e) {
			log.error("[StudentController addStudentWithCourse]", e);
			dto.setCode(ResCode.FAIL.value());
			dto.setMessage("Fail: setRegisteredStudentWithNewCourse");
		}
		return dto;
	}

	// [수강생 정보] - 수강생 정보 수정
	@PutMapping("/students")
	public UpdateDeleteResultDTO updateSelectedStudentInfo(@RequestBody UpdateSelectedStudentInfoDTO request) {
		UpdateDeleteResultDTO dto = new UpdateDeleteResultDTO();
		try {
			studentServiceImpl.updateSelectedStudentInfo(request.getName(), request.getAddress(), request.getBank(), request.getAccount(), request.getPhoneNumber(), request.getEmail(), request.getStudentId());
		} catch (NoSuchDataException e) {
			dto.setCode(ResCode.FAIL.value());
			dto.setMessage("Fail: updateSelectedStudentInfo");
		} catch (Exception e) {
			log.error("[StudentController updateSelectedStudentInfo]", e);
			dto.setCode(ResCode.FAIL.value());
			dto.setMessage("Fail: updateSelectedStudentInfo");
		}
		return dto;
	}
	
	// [수강생 정보] - 수강생 삭제
	@PatchMapping("students/active-status")
	// public UpdateDeleteResultDTO deleteSelectedStudent(@PathVariable String studentId) {
	public UpdateDeleteResultDTO deleteSelectedStudent(@RequestBody UpdateSelectedStudentInfoDTO request) {
		UpdateDeleteResultDTO dto = new UpdateDeleteResultDTO();
		try {
			studentServiceImpl.removeSelectedStudent(request.getStudentId());
		} catch (NoSuchDataException e) {
			dto.setCode(ResCode.FAIL.value());
			dto.setMessage("Fail: updateSelectedStudentInfo");
		} catch (Exception e) {
			log.error("[StudentController updateSelectedStudentInfo]", e);
			dto.setCode(ResCode.FAIL.value());
			dto.setMessage("Fail: updateSelectedStudentInfo");
		}
		return dto;
	}
}