package com.kosta.ems.student;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.kosta.ems.manager.ManagerDTO;
import com.kosta.ems.manager.ManagerService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import lombok.extern.log4j.Log4j2;

@Log4j2
@RestController
@RequestMapping("/students")
@RequiredArgsConstructor
public class StudentController {
	private final StudentService studentService;
	private final ManagerService managerService;
	@Value("OFF")
	private String SECURITY_LEVEL;


	// [수강생 정보] - 수강생 정보 조회
	// * 0715 _managerId 연결 및 테스트코드 정리 완료
	@PostMapping("/student-list")
	public Map<String, Object> getStudentsByNameOrCourseNumber(@RequestParam(name="page", required = false, defaultValue = "1") int page, @RequestBody StudentInfoDTO dto) {
		Map<String, Object> result = new HashMap<String, Object>();
		int size = 10;
		int courseNumber = dto.getCourseNumber();
		log.info("☄️☄️ request courseNumber: " + courseNumber);
		String name = dto.getName().equals("") ? "" : dto.getName();
		log.info("☄️☄️ request name: " + name);
		int isActive = dto.getIsActive();  // 임시
		log.info("☄️☄️ request isActive: " + isActive);
		String academyLocation = getAcademyOfLoginUser();
		log.info("☄️☄️ request academyLocation: " + academyLocation);

		int totalCount = studentService.getStudentInfoListCnt(isActive, name, courseNumber, academyLocation);
		result.put("amount", totalCount);
		// result.put("studentList", studentService.getStudentInfoList(isActive, name, courseNumber, academyLocation, page, size));
		result.put("studentList", studentService.getStudentInfoList2(isActive, name, courseNumber, academyLocation, page, size));

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
	// *0710 선택 수강생의 수강내역 조회
	@PostMapping("/student-course-history")
	public Map<String, Object> getStudentCourseHistory(@RequestBody StudentInfoDTO request) {
		Map<String, Object> result = new HashMap<String, Object>();

		log.info("🙃 request.studentId(): " + request.getStudentId());
		result.put("studentCourseHistory", studentService.getStudentCourseHistory(request.getStudentId()));
		log.info("🙃 studentCourseHistory"+studentService.getStudentCourseHistory(request.getStudentId()));
		return result;
	}

	// [수강생 정보] - 수강생 등록
	// 1. 등록된 hrdNetId인지 확인 _POSTMAN 확인 완료 -- 비동기
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
	// * 0715 _managerId 연결 및 테스트코드 정리 완료
	@GetMapping("/on-going-courses")
	public Map<String, Object> getOnGoingCourseList() {
		Map<String, Object> result = new HashMap<String, Object>();
		String academyLocation = getAcademyOfLoginUser();
		result.put("courseList", studentService.getOnGoingCourseList(academyLocation));
		return result;
	}

	// [수강생 정보] - 수강생 등록
	// 3-1. 신규 수강생 등록  // *0715_안 쓰기로 결정...
	/*
	@PostMapping()
	public UpdateDeleteResultDTO setStudentWithCourse(@RequestBody RequestAddStudentBasicInfoDTO request) {
		UpdateDeleteResultDTO dto = new UpdateDeleteResultDTO();
		// String managerId = "e84dea58-3784-11ef-b0b2-0206f94be675";  // Name: 테스트용, pw: 1234, 교육장: 가산
		String managerId = getManagerIdOfLoginUser();
		log.info("💥managerId: " + managerId);

		try {
			studentService.setStudentWithCourse(request.getHrdNetId(), request.getName(), request.getBirth(), request.getAddress(), request.getBank(), request.getAccount(), request.getPhoneNumber(), request.getEmail(), request.getGender(), managerId, request.getCourseNumber());
		} catch (NoSuchDataException e) {
			dto.setCode(ResCode.FAIL.value());
			dto.setMessage("Fail: setStudentWithCourse");
		} catch (Exception e) {
			log.error("[StudentController addStudentWithCourse]", e);
		}
		return dto;
	}
	*/

	// [수강생 정보] - 수강생 등록
	// 3-2. 기존 수강생의 과정 수강 신규 등록
	@PostMapping("/new-course")
	public UpdateDeleteResultDTO setRegisteredStudentWithNewCourse(@RequestBody RequestAddStudentBasicInfoDTO request) {
		UpdateDeleteResultDTO dto = new UpdateDeleteResultDTO();
		String managerId = getManagerIdOfLoginUser();
		log.info("💥managerId: " + managerId);

		try {
			studentService.setStudentCourseSeqInfo(request.getHrdNetId(), request.getCourseNumber(), managerId);
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
	// 1. 선택한 수강생의 등록된 정보 불러오기
	// @Controller에서 작업

	// 2. 페이지 양식에서 작성한 내용으로 수강생 정보 수정하기
	@PutMapping()
	public UpdateDeleteResultDTO updateSelectedStudentInfo(@RequestBody UpdateSelectedStudentInfoDTO request) {
		UpdateDeleteResultDTO dto = new UpdateDeleteResultDTO();
		try {
			studentService.updateSelectedStudentInfo(request.getName(), request.getAddress(), request.getBank(), request.getAccount(), request.getPhoneNumber(), request.getEmail(), request.getStudentId(), request.getIsActive());
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

	private String getAcademyOfLoginUser() {
		if(SECURITY_LEVEL.equals("OFF")) {
			return "가산";
			// return "강남";
		}
		ManagerDTO loginUser = getLoginUser();
		return loginUser.getAcademyLocation();
	}

	private String getManagerIdOfLoginUser() {
		if(SECURITY_LEVEL.equals("OFF")) {
			return "3ddf8577-3eaf-11ef-bd30-0206f94be675";
			// 강남직원1:
			// return "3ddf8703-3eaf-11ef-bd30-0206f94be675";
		}
		ManagerDTO loginUser = getLoginUser();
		return loginUser.getManagerId();
	}

	private ManagerDTO getLoginUser() {
		ManagerDTO loginUser;
		if (SECURITY_LEVEL.equals("OFF")) {
			loginUser = managerService.findByEmployeeNumber("EMP0002");
			// 강남직원1:
			// loginUser = managerService.findByEmployeeNumber("EMP0006");
		} else {
			loginUser = (ManagerDTO) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		}
		return loginUser;
	}
}