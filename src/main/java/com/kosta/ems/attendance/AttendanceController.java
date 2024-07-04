package com.kosta.ems.attendance;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.kosta.ems.student.NoSuchDataException;
import com.kosta.ems.student.PageRequestDTO;
import com.kosta.ems.student.PageResponseDTO;
import com.kosta.ems.student.ResCode;
import com.kosta.ems.student.UpdateDeleteResultDTO;

import lombok.extern.log4j.Log4j2;

@Log4j2
@RestController
@RequestMapping("/attendances")
@RequiredArgsConstructor
public class AttendanceController {
	// @Autowired
	// private AttendanceServiceImpl attendanceServiceImpl;
	private final AttendanceService attendanceService;

	// [출결] - 수강생 출석 조회 목록 조회 __POSTMAN 테스트 완료 __예외 처리 고려 필요!
	@PostMapping("/student-list")
	@ResponseBody
	public Map<String, Object> getStudentAttendanceList(@RequestParam(name="page", required=false, defaultValue="1") int page, @RequestBody RequestStudentAttendanceDTO dto) {
		Map<String, Object> result = new HashMap<String, Object>();
		int size=10;
		int totalCount = 0;
		log.info(">> getStudentAttendanceList");
		log.info(">> -- courseNumber: " + dto.getCourseNumber());
		log.info(">> -- name: " + dto.getName());
		log.info(">> -- page: " + page);
		// log.info(">> -- pageInfo: " + pageRequest.toString());

		if(!dto.getName().equals("none") && dto.getCourseNumber() != -1) {
			// 기수, 수강생명 모두 입력해 검색
			totalCount = attendanceService.getAttendanceIntegratedListFilterAllAmount(dto.getName(), dto.getCourseNumber());

			// 수강생 출결 목록 데이터
			result.put("attendanceList", attendanceService.getAttendanceIntegratedListFilterAll(dto.getName(), dto.getCourseNumber(), page, size));
			result.put("amount", totalCount);
			result.put("searchCourseNumber", dto.getCourseNumber());
			result.put("searchStudentName", dto.getName());
		} else if(dto.getName().equals("none") && dto.getCourseNumber() == -1) {
			// 기수, 수강생명 미입력 검색 (전체 데이터)
			totalCount = attendanceService.getAttendanceIntegratedListNoFilterAmount(dto.getName(), dto.getCourseNumber());
			result.put("attendanceList", attendanceService.getAttendanceIntegratedListNoFilter(dto.getName(), dto.getCourseNumber(), page, size));
			result.put("amount", totalCount);
			result.put("searchCourseNumber", dto.getCourseNumber());
			result.put("searchStudentName", dto.getName());
		} else if((!dto.getName().equals("none") && dto.getCourseNumber() == -1) || (dto.getName().equals("none") && dto.getCourseNumber() != -1)) {
			// 기수 또는 수강생명 입력하여 검색
			log.info("else if 또는");
			totalCount = attendanceService.getAttendanceIntegratedListFilterAmount(dto.getName(), dto.getCourseNumber());
			// 수강생 출결 목록 데이터
			result.put("attendanceList", attendanceService.getAttendanceIntegratedListFilter(dto.getName(), dto.getCourseNumber(), page, size));
			result.put("amount", totalCount);
			result.put("searchCourseNumber", dto.getCourseNumber());
			result.put("searchStudentName", dto.getName());
		}

		// 페이징 response
		int totalPage = (totalCount/size) + 1;
		// int currentPage = pageRequest.getCurrentPage();
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

		log.info(">> -- pageInfo: " + result.get("pageInfo").toString());

		return result;
	}

	// [출결] - 출결 검색(조건: 날짜, 기수, 수강생명) 데이터 목록 가져오기 -- POSTMAN 테스트 완료
	@PostMapping("/search-list")
	public Map<String, Object> getFilteredAttendanceList(@RequestParam(name="page", required = false, defaultValue = "1") int page, @RequestBody RequestStudentAttendanceDTO dto) {
	// public Map<String, Object> getFilteredAttendanceList(@RequestBody Map<String, Object> request) {
		Map<String, Object> result = new HashMap<String, Object>();

		// request JSON 분리 (pageRequest, attendanceRequest)
		// Map<String, Object> pageRequest = (Map<String, Object>) request.get("pageRequest");
		// Map<String, Object> attendanceRequest = (Map<String, Object>) request.get("attendanceRequest");

		int size = 10;

		// 검색 경우(1~3) 파악
		// int courseNumber = (int) attendanceRequest.get("courseNumber");
		// String name = (String) attendanceRequest.get("name") == "" ? "none" : (String) attendanceRequest.get("name");
		int courseNumber = dto.getCourseNumber();
		String name = dto.getName().equals("") ? "none" : dto.getName();
		int totalCount = 0;
		// String academyLocation = dto.getAcademyLocation();
		String academyLocation = "가산";

		if(courseNumber != -1 && !name.equals("none")) {
			// 경우1: 기수, 수강생명 모두 입력한 검색
			totalCount = attendanceService.getCourseNumberAndStudentNameListAmount(dto.getAttendanceDate(), academyLocation, dto.getName(), dto.getCourseNumber());

			// 수강생 출결 목록 데이터
			result.put("attendanceList", attendanceService.getCourseNumberAndStudentNameList(dto.getAttendanceDate(), academyLocation, dto.getName(), dto.getCourseNumber(), page, size));
			result.put("amount", totalCount);
		} else if(courseNumber == -1 && name.equals("none")) {
			// 경우3: 기수, 수강생명 모두 미입력 (날짜만) 검색
			totalCount = attendanceService.getDateAndLocationListAmount(dto.getAttendanceDate(), academyLocation, dto.getName(), dto.getCourseNumber());

			// 수강생 출결 목록 데이터
			result.put("attendanceList", attendanceService.getDateAndLocationList(dto.getAttendanceDate(), academyLocation, dto.getName(), dto.getCourseNumber(), page, size));
			result.put("amount", totalCount);
		} else {
			// 경우2: 기수 또는 수강생명 입력한 검색
			totalCount = attendanceService.getCourseNumberOrStudentNameListAmount(dto.getAttendanceDate(), academyLocation, dto.getName(), dto.getCourseNumber());

			// 수강생 출결 목록 데이터
			result.put("attendanceList", attendanceService.getCourseNumberOrStudentNameList(dto.getAttendanceDate(), academyLocation, dto.getName(), dto.getCourseNumber(), page, size));
			result.put("amount", totalCount);
		}

		// 페이징 response
		int totalPage = (totalCount/size) + 1;
		// int currentPage = (int)pageRequest.get("currentPage");
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

	// [출결] - 선택한 수강생의 출석 상태 수정, 입력
	@PutMapping("/attendance-status")
	public UpdateDeleteResultDTO updateStudentAttendance(@RequestBody List<RequestStudentAttendanceDTO> request) {
		UpdateDeleteResultDTO dto = new UpdateDeleteResultDTO();
		try {
			log.info("🚀 request 확인");
			log.info(">> request.length: " + request.size());
			log.info(">> request: " + request.toString());
			for(int i=0; i<request.size(); i++) {
				attendanceService.updateStudentAttendance(request.get(i).getAttendanceStatus(), request.get(i).getAttendanceDate(), request.get(i).getStudentCourseSeq());
			}
		} catch (NoSuchDataException e) {
			dto.setCode(ResCode.FAIL.value());
			dto.setMessage("Fail: updateStudentAttendance");
		} catch (Exception e) {
			log.error("[StudentController updateStudentAttendance]", e);
			dto.setCode(ResCode.FAIL.value());
			dto.setMessage("Fail: updateSelectedStudentInfo");
		}
		return dto;
	}

	// [출결 입력] - 특정일의 출결 상태가 등록되지 않은 수강생 목록 데이터 가져오기
	// [메모] 관리자 로그인 시, 로그인한 관리자의 교육장을 조회하여 매개변수에 넣는 작업 필요할 듯.
	@GetMapping("/no-attendance-list")
	public Map<String, Object> getNoAttendanceStatusList(@RequestParam(name="attendanceDate") String attendanceDate) {
		Map<String, Object> result = new HashMap<String, Object>();
		String academyLocation = "가산";  // [메모] 관리자 로그인 시, 로그인한 관리자의 교육장을 조회하여 매개변수에 넣고, 그걸 여기에 넣어야할 것 같음.

		result.put("studentList", attendanceService.getNoAttendanceStatusStudentList(attendanceDate, academyLocation));

		return result;
	}

	@PostMapping("/attendance-status")
	public Map<String, Object> setStudentAttendance(@RequestBody List<RequestStudentAttendanceDTO> request) {
		Map<String, Object> result = new HashMap<String, Object>();
		UpdateDeleteResultDTO dto = new UpdateDeleteResultDTO();
		try {
			log.info("🚀 request 확인");
			log.info(">> request.length: " + request.size());
			log.info(">> request: " + request.toString());
			for(int i=0; i<request.size(); i++) {
				attendanceService.setAttendanceStatus(request.get(i).getAttendanceStatus(), request.get(i).getAttendanceDate(), request.get(i).getStudentCourseSeq());
			}
			dto.setCode(ResCode.SUCCESS.value());
			dto.setMessage("Success: setStudentAttendance");
			result.put("code", dto.getCode());
			result.put("message", dto.getMessage());
		} catch (NoSuchDataException e) {
			dto.setCode(ResCode.FAIL.value());
			dto.setMessage("Fail: setStudentAttendance");
			result.put("code", dto.getCode());
			result.put("message", dto.getMessage());
		} catch (Exception e) {
			log.error("[AttendanceController setStudentAttendance]", e);
			dto.setCode(ResCode.FAIL.value());
			dto.setMessage("Fail: setStudentAttendance");
			result.put("code", dto.getCode());
			result.put("message", dto.getMessage());
		}
		return result;
	}
}
