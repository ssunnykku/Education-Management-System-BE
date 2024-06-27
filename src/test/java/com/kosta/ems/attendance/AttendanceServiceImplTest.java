package com.kosta.ems.attendance;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Slf4j
class AttendanceServiceImplTest {

    @Autowired
    AttendanceService AttendanceServiceImpl;

//    @Test
//    void getAttendanceByStudentIdAndDurationTest() {
//        List<AttendanceStudentCourseDTO> attendance = (ArrayList<AttendanceStudentCourseDTO>) AttendanceServiceImpl.getAttendanceByStudentIdAndDuration("2024-03-03", "2024-04-04", "efa146c5-2fa7-11ef-b0b2-0206f94be675");
//
//        for (AttendanceStudentCourseDTO a : attendance) {
//            log.info(a.getAttendanceStatus());
//        }
//    }
    
    // [출결] - 수강생 출결 조회 목록 데이터 개수 (for 페이지네이션)
    // @Test
    void getStudentAttendanceListAmount() {
    	log.info(Integer.toString(AttendanceServiceImpl.getStudentAttendanceListAmount("유", 277)));    
    }
    
    // [출결] - 수강생 출석 조회 목록 조회
    // @Test
    void getStudentAttendanceList() {
    	log.info(AttendanceServiceImpl.getStudentAttendanceList("유", 277, 3, 2).toString());
    }
    
    // [출결] - 특정일의 수강생 출석 상태 목록 조회 (for 출결 입력/수정)
    // 경우1 _ 기수+수강생명 입력
    // 검색 결과 개수 가져오기 (for 페이지네이션)
    // @Test
    void selectCourseNumberAndStudentNameListAmount() {
    	System.out.println(AttendanceServiceImpl.selectCourseNumberAndStudentNameListAmount("2024-06-21", "가산", "유", 277));
    	// log.info(Integer.toString(AttendanceServiceImpl.selectCourseNumberAndStudentNameListAmount("2024.06.21", "가산", "유", 277)));
    }
    // 검색 결과 데이터 목록 가져오기
    // @Test
    void selectCourseNumberAndStudentNameList() {
    	log.info(AttendanceServiceImpl.selectCourseNumberAndStudentNameList("2024-06-21", "가산", "유", 277, 0, 2).toString());
    }
    
    // 경우2 _ 기수 또는 수강생명 입력
    // 검색 결과 개수 가져오기 (for 페이지네이션)
    // @Test
    void selectCourseNumberOrStudentNameListAmount() {
    	log.info(Integer.toString(AttendanceServiceImpl.selectCourseNumberOrStudentNameListAmount("2024-06-21", "가산", "철", -1)));
    }
    // 검색 결과 데이터 목록 가져오기
    // @Test
    void selectCourseNumberOrStudentNameList() {
    	log.info(AttendanceServiceImpl.selectCourseNumberOrStudentNameList("2024-06-21", "가산", "철", -1, 0, 2).toString());
    }
    
    // 경우3 _ 기수+수강생명 미입력
    // 검색 결과 개수 가져오기 (for 페이지네이션)
    // @Test
    void selectDateAndLocationListAmount() {
    	log.info(Integer.toString(AttendanceServiceImpl.selectDateAndLocationListAmount("2024-06-21", "가산", "none", -1)));
    }
    // 검색 결과 데이터 목록 가져오기
    // @Test
    void selectDateAndLocationList() {
    	log.info(AttendanceServiceImpl.selectDateAndLocationList("2024-06-21", "가산", "none", -1, 0, 2).toString());
    }
    
    // [출결] - 선택한 수강생의 출석 상태 수정
 	// @Test
     void updateStudentAttendance() {
    	 AttendanceServiceImpl.updateStudentAttendance("출석", "2024-06-21", "efa148aa-2fa7-11ef-b0b2-0206f94be675");
     }
}