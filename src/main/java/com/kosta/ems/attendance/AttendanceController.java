package com.kosta.ems.attendance;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.kosta.ems.student.NoSuchDataException;
import com.kosta.ems.student.PageRequestDTO;
import com.kosta.ems.student.PageResponseDTO;
import com.kosta.ems.student.ResCode;
import com.kosta.ems.student.UpdateDeleteResultDTO;

import lombok.extern.log4j.Log4j2;

@Log4j2
@RestController
public class AttendanceController {
	@Autowired
	private AttendanceServiceImpl attendanceServiceImpl;
	
	// [출결] - 수강생 출석 조회 목록 조회 __예외 처리 고려 필요!
	@GetMapping("attendances/student-list/{name}/{courseNumber}")
	public Map<String, Object> getStudentAttendanceList(@PathVariable("name") String name, @PathVariable("courseNumber") int courseNumber, @RequestBody PageRequestDTO pageRequest) {
		Map<String, Object> result = new HashMap<String, Object>();
		int page = pageRequest.getPage()-1;  // page는 실제로 0부터 시작하기 때문
		int size = pageRequest.getSize();

		// 페이징 response
		int totalCount = attendanceServiceImpl.getStudentAttendanceListAmount(name, courseNumber);
		int totalPage = (totalCount/size) + 1;
		// int currentPage = pageRequest.getCurrentPage();
		int currentPage = 2;  // 브라우저에서 받아올 값인데 아직 연결안해서 controller 테스트를 위해 작성했던 코드.
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
		
		// 수강생 출결 목록 데이터
		result.put("data", attendanceServiceImpl.getStudentAttendanceList(name, courseNumber, page, size));
		
		return result;
	}

	// [출결] - 출결 검색(조건: 날짜, 기수, 수강생명) 데이터 목록 가져오기 -- POSTMAN 테스트 완료
	@PostMapping("attendances/search-list")
	public Map<String, Object> getFilteredAttendanceList(@RequestBody Map<String, Object> request) {
		Map<String, Object> result = new HashMap<String, Object>();

		// request JSON 분리 (pageRequest, attendanceRequest)
		Map<String, Object> pageRequest = (Map<String, Object>) request.get("pageRequest");
		Map<String, Object> attendanceRequest = (Map<String, Object>) request.get("attendanceRequest");

		int page = ((int) pageRequest.get("page"))-1;  // page는 실제로 0부터 시작하기 때문
		int size = (int) pageRequest.get("size");

		// 검색 경우(1~3) 파악
		int courseNumber = (int) attendanceRequest.get("courseNumber");
		String name = (String) attendanceRequest.get("name") == "" ? "none" : (String) attendanceRequest.get("name");
		int totalCount = 0;

		if(courseNumber != -1 && name == "none") {
			// 경우1: 기수, 수강생명 모두 입력한 검색
			System.out.println(">> 경우1");
			totalCount = attendanceServiceImpl.selectCourseNumberAndStudentNameListAmount((String) attendanceRequest.get("attendanceDate"), (String) attendanceRequest.get("academyLocation"), (String) attendanceRequest.get("name"), (int)attendanceRequest.get("courseNumber"));

			// 수강생 출결 목록 데이터
			result.put("data", attendanceServiceImpl.selectCourseNumberAndStudentNameList((String) attendanceRequest.get("attendanceDate"), (String) attendanceRequest.get("academyLocation"), (String) attendanceRequest.get("name"), (int) attendanceRequest.get("courseNumber"), page, size));
		} else if(courseNumber == -1 && name == "none") {
			// 경우3: 기수, 수강생명 모두 미입력 (날짜만) 검색
			System.out.println(">> 경우3");
			totalCount = attendanceServiceImpl.selectDateAndLocationListAmount((String) attendanceRequest.get("attendanceDate"), (String) attendanceRequest.get("academyLocation"), (String) attendanceRequest.get("name"), (int)attendanceRequest.get("courseNumber"));

			// 수강생 출결 목록 데이터
			result.put("data", attendanceServiceImpl.selectDateAndLocationList((String) attendanceRequest.get("attendanceDate"), (String) attendanceRequest.get("academyLocation"), (String) attendanceRequest.get("name"), (int) attendanceRequest.get("courseNumber"), page, size));
		} else {
			// 경우2: 기수 또는 수강생명 입력한 검색
			System.out.println(">> 경우2");
			totalCount = attendanceServiceImpl.selectCourseNumberOrStudentNameListAmount((String) attendanceRequest.get("attendanceDate"), (String) attendanceRequest.get("academyLocation"), (String) attendanceRequest.get("name"), (int)attendanceRequest.get("courseNumber"));

			// 수강생 출결 목록 데이터
			result.put("data", attendanceServiceImpl.selectCourseNumberOrStudentNameList((String) attendanceRequest.get("attendanceDate"), (String) attendanceRequest.get("academyLocation"), (String) attendanceRequest.get("name"), (int) attendanceRequest.get("courseNumber"), page, size));
		}

		// 페이징 response
		int totalPage = (totalCount/size) + 1;
		int currentPage = (int)pageRequest.get("currentPage");
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

	// [출결] - 선택한 수강생의 출석 상태 수정
	@PutMapping("attendances/student-status")
	public UpdateDeleteResultDTO updateStudentAttendance(@RequestBody RequestStudentAttendanceDTO request) {
		UpdateDeleteResultDTO dto = new UpdateDeleteResultDTO();
		try {
			int year = Integer.parseInt(request.getAttendanceDate().split(".")[0]);
			int month = Integer.parseInt(request.getAttendanceDate().split(".")[1]);
			int day = Integer.parseInt(request.getAttendanceDate().split(".")[2]);
			
			// attendanceServiceImpl.updateStudentAttendance(request.getAttendanceStatus(), LocalDate.of(year, month, month), request.getStudentId());
			attendanceServiceImpl.updateStudentAttendance(request.getAttendanceStatus(), request.getAttendanceDate(), request.getStudentId());
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
}
