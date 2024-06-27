package com.kosta.ems.attendance;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Slf4j
@SpringBootTest
class AttendanceMapperTest {

    @Autowired
    private AttendanceMapper attendanceMapper;
    
    // [출결] - 수강생 출석 조회 목록 데이터 개수 (for 페이지네이션)
    // @Test
    public void selectStudentAttendanceListAmount() {
    	log.info(attendanceMapper.selectStudentAttendanceListAmount("유", 277).toString());
    }
    
    // [출결] - 수강생 출석 조회 목록 조회
    // @Test
    public void selectStudentAttendanceList() {
    	log.info(attendanceMapper.selectStudentAttendanceList("유", 277, 0, 2).toString());
    }
    
    // [출결] - 특정일의 수강생 출석 상태 목록 조회 (for 출결 입력/수정)
    // 경우1 _ 기수+수강생명 입력
    // 검색 결과 개수 가져오기 (for 페이지네이션)
    //@Test  // 패스
    public void selectCourseNumberAndStudentNameListAmount() {
    	log.info(Integer.toString(attendanceMapper.selectCourseNumberAndStudentNameListAmount(LocalDate.parse("2024-06-21"), "가산", "유", 277)));

    }
    // 검색 결과 데이터 목록 가져오기
    @Test
    public void selectCourseNumberAndStudentNameList() {
    	Collection<AttendanceListBySearchFilterDTO> list= (ArrayList<AttendanceListBySearchFilterDTO>) attendanceMapper.selectCourseNumberAndStudentNameList(LocalDate.parse("2024-06-21"), "가산", "유", 277, 0, 2);
    	for (AttendanceListBySearchFilterDTO dto : list) {
    		System.out.println("asdf");
			System.out.println(dto);
		}
    	//log.info(attendanceMapper.selectCourseNumberAndStudentNameList(LocalDate.of(2024, 6, 21), "가산", "유", 277, 0, 2).toString());

    }
    
    // 경우2 _ 기수 또는 수강생명 입력
    // 검색 결과 개수 가져오기 (for 페이지네이션)
    // @Test
    public void selectCourseNumberOrStudentNameListAmount() {
    	log.info(Integer.toString(attendanceMapper.selectCourseNumberOrStudentNameListAmount(LocalDate.parse("2024-06-21"), "가산", "철", -1)));
    }
    // 검색 결과 데이터 목록 가져오기
    // @Test
    public void selectCourseNumberOrStudentNameList() {
    	log.info(attendanceMapper.selectCourseNumberOrStudentNameList(LocalDate.of(2024, 6, 21), "가산", "철", 10, 0, 2).toString());
    	// log.info(attendanceMapper.selectCourseNumberOrStudentNameList(LocalDate.of(2024, 6, 21), "가산", "철", -1, 0, 2).toString());
    }
    
    // 경우3 _ 기수+수강생명 미입력
    // 검색 결과 개수 가져오기 (for 페이지네이션)
    // @Test  // 패스
    public void selectDateAndLocationListAmount() {
    	log.info(Integer.toString(attendanceMapper.selectDateAndLocationListAmount(LocalDate.of(2024, 6, 21), "가산", "none", -1)));
    }
    // 검색 결과 데이터 목록 가져오기
    // @Test
    public void selectDateAndLocationList() {
    	log.info(attendanceMapper.selectDateAndLocationList(LocalDate.of(2024, 6, 21), "가산", "none", 10, 0, 2).toString());
    }
    
    
    // [출결] - 선택한 수강생의 출석 상태 수정
    // @Test
    void updateStudentAttendance() {
    	attendanceMapper.updateStudentAttendance(new UpdateStudentAttendanceStatusDTO("지각", LocalDate.of(2024, 06, 21), "efa148aa-2fa7-11ef-b0b2-0206f94be675"));
    }
    
    
    @Test
    void selectCourseNumberAndStudentNameListAmountTest() {
    	assertThat(attendanceMapper.selectCourseNumberAndStudentNameListAmount(LocalDate.parse("2024-06-21"),"가산", "유", 277)).isEqualTo(2);
    	
    }
}



