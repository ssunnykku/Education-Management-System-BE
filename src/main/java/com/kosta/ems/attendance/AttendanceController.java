package com.kosta.ems.attendance;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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
		// int totalCount = attendanceServiceImpl.getStudentAttendanceList(name, courseNumber);
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
	
	// [출결] - 선택한 수강생의 출석 상태 수정
	@PutMapping("attendances/student-status")
	public UpdateDeleteResultDTO updateStudentAttendance(@RequestBody RequestStudentAttendanceDTO request) {
		UpdateDeleteResultDTO dto = new UpdateDeleteResultDTO();
		try {
			int year = Integer.parseInt(request.getAttendanceDate().split(".")[0]);
			int month = Integer.parseInt(request.getAttendanceDate().split(".")[1]);
			int day = Integer.parseInt(request.getAttendanceDate().split(".")[2]);
			
			attendanceServiceImpl.updateStudentAttendance(request.getAttendanceStatus(), LocalDate.of(year, month, month), request.getStudentId());
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
