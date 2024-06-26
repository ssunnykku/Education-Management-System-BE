package com.kosta.ems.attendance;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
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
    @Test
    void getStudentAttendanceListAmount() {
    	log.info(Integer.toString(AttendanceServiceImpl.getStudentAttendanceListAmount("유", 277)));    
    }
    
    // [출결] - 수강생 출석 조회 목록 조회
    // @Test
    void getStudentAttendanceList() {
    	log.info(AttendanceServiceImpl.getStudentAttendanceList("유", 277, 3, 2).toString());
    }
    
    // [출결] - 선택한 수강생의 출석 상태 수정
 	// @Test
     void updateStudentAttendance() {
    	 AttendanceServiceImpl.updateStudentAttendance("출석", "2024-06-21", "efa148aa-2fa7-11ef-b0b2-0206f94be675");
     }
}