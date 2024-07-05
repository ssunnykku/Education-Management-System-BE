package com.kosta.ems.student;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import lombok.extern.log4j.Log4j2;

@Log4j2
@RestController
@RequestMapping("/students")
@RequiredArgsConstructor
public class StudentController {
	private final StudentService studentService;

	/*
	// [수강생 정보] - 수강생 정보 조회  _POSTMAN 확인 완료
	@GetMapping("/students/student-list/{name}/{courseNumber}")
	public List<StudentBasicInfoDTO> getStudentsByNameOrCourseNumber(@PathVariable String name, @PathVariable int courseNumber) {
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
	*/

	// [수강생 정보] - 수강생 정보 조회
	@PostMapping("/student-list")
	public Map<String, Object> getStudentsByNameOrCourseNumber(@RequestParam(name="page", required = false, defaultValue = "1") int page, @RequestBody AddStudentBasicInfoDTO dto) {
		Map<String, Object> result = new HashMap<String, Object>();
		int size = 10;
		int courseNumber = dto.getCourseNumber();
		String name = dto.getName().equals("") ? "" : dto.getName();

		int totalCount = studentService.getStudentsByNameOrCourseNumberAmount(name, courseNumber);
		result.put("amount", totalCount);
		result.put("studentList", studentService.getStudentsByNameOrCourseNumberList(name, courseNumber, page, size));

		// 페이징 response
		int totalPage = (totalCount/size) + 1;
		int currentPage = 1;
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

		return result;
	}

	// [수강생 정보] - 수강생 등록
	// 1. 등록된 hrdNetId인지 확인 _POSTMAN 확인 완료 -- 비동기
	// @GetMapping("/students/valid-id")
	@PostMapping("/valid-id")
	public Map<String, Object> findByHrdNetId(@RequestBody AddStudentBasicInfoDTO request) {
		Map<String, Object> result = new HashMap<String, Object>();
		String hrdNetId = request.getHrdNetId();
		
		boolean check = studentService.findByHrdNetId(request.getHrdNetId());
		// check: true - 등록 이력 있는 수강생, false - 신규 수강생 등록

		if(check == false) {
			result.put("result", String.valueOf(check));
		} else {
			// 등록된 수강생 기본 정보 불러오기
			result.put("result", String.valueOf(check));
			RegisteredStudentInfoDTO dto = studentService.getRegisteredStudentBasicInfo(hrdNetId);
			result.put("data", dto);
		}
		return result;
	}

	// [수강생 정보] - 수강생 등록
	// 2. 현재 진행 중인 수강신청 가능한 교육과정 목록 불러오기
	@GetMapping("/on-going-courses")
	// spring security 적용 후에는 principal 로 로그인한 매니저의 교육장을 받아오는 걸로 변경해야함.
	public Map<String, Object> getOnGoingCourseList() {
		Map<String, Object> result = new HashMap<String, Object>();
		String academyLocation = "가산";  // spring security 적용 전, 임시 코드
		result.put("courseList", studentService.getOnGoingCourseList(academyLocation));
		return result;
	}

	// [수강생 정보] - 수강생 등록
	// 3-1. 신규 수강생 등록
	@PostMapping()
	// @RequestBody로 등록 양식 정보 받고, @RequestParam에 '신규 가입', '기존 수강생+수강신청' 여부를 문자로 체킹할 수 있도록 하는게 나을듯함
	public UpdateDeleteResultDTO setStudentWithCourse(@RequestBody RequestAddStudentBasicInfoDTO request) {
		UpdateDeleteResultDTO dto = new UpdateDeleteResultDTO();
		try {
			studentService.setStudentWithCourse(request.getHrdNetId(), request.getName(), request.getBirth(), request.getAddress(), request.getBank(), request.getAccount(), request.getPhoneNumber(), request.getEmail(), request.getGender(), request.getManagerId(), request.getCourseNumber());
		} catch (NoSuchDataException e) {
			dto.setCode(ResCode.FAIL.value());
			dto.setMessage("Fail: setStudentWithCourse");
		} catch (Exception e) {
			log.error("[StudentController addStudentWithCourse]", e);
		}
		return dto;
	}
	
	// [수강생 정보] - 수강생 등록
	// 3-2. 기존 수강생의 과정 수강 신규 등록 -- POSTMAN 테스트 완료
	@PostMapping("/new-course")
	public UpdateDeleteResultDTO setRegisteredStudentWithNewCourse(@RequestBody RequestAddStudentBasicInfoDTO request) {
		UpdateDeleteResultDTO dto = new UpdateDeleteResultDTO();
		try {
			log.info(">>> getHrdNetID() ");
			log.info(request.getHrdNetId());
			log.info(">>> getCourseNumber() ");
			log.info(request.getCourseNumber());
			studentService.setStudentCourseSeqInfo(request.getHrdNetId(), request.getCourseNumber());
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
			studentService.updateSelectedStudentInfo(request.getName(), request.getAddress(), request.getBank(), request.getAccount(), request.getPhoneNumber(), request.getEmail(), request.getStudentId());
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
	@PatchMapping("/active-status")
	// public UpdateDeleteResultDTO deleteSelectedStudent(@PathVariable String studentId) {
	public UpdateDeleteResultDTO deleteSelectedStudent(@RequestBody UpdateSelectedStudentInfoDTO request) {
		UpdateDeleteResultDTO dto = new UpdateDeleteResultDTO();
		try {
			studentService.removeSelectedStudent(request.getStudentId());
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