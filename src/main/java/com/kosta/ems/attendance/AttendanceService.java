package com.kosta.ems.attendance;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collection;

@Service
public interface AttendanceService {
    int getNumberOfAttendance(LocalDate startDate, LocalDate endDate, String studentId);

    int getNumberOfLeave(LocalDate startDate, LocalDate endDate, String studentId);


    /* 훈련 수당 대상자 :  출석률 80%*/

    // [출결] - 수강생 출석 조회 목록 조회
    Collection<StudentAttendanceListDTO> getStudentAttendanceList(String name, String courseNumber);
    
    // [출결] - 선택한 수강생의 출석 상태 수정
    void updateStudentAttendance(String attendanceStatus, String attendanceDate, String studentId);
}
