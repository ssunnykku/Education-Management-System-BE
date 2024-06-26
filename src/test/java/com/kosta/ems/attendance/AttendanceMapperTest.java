package com.kosta.ems.attendance;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

@Slf4j
@SpringBootTest
class AttendanceMapperTest {

    @Autowired
    private AttendanceMapper attendanceMapper;
    
    // [출결] - 수강생 출석 조회 목록 데이터 개수 (for 페이지네이션)
    @Test
    public void selectStudentAttendanceListAmount() {
    	log.info(attendanceMapper.selectStudentAttendanceListAmount("유", 277).toString());
    }
    
    // [출결] - 수강생 출석 조회 목록 조회
    // @Test
    public void selectStudentAttendanceList() {
    	log.info(attendanceMapper.selectStudentAttendanceList("유", 277, 0, 2).toString());
    }
    
    // [출결] - 선택한 수강생의 출석 상태 수정
    // @Test
    void updateStudentAttendance() {
    	attendanceMapper.updateStudentAttendance(new UpdateStudentAttendanceStatusDTO("지각", LocalDate.of(2024, 06, 21), "efa148aa-2fa7-11ef-b0b2-0206f94be675"));
    }
}