package com.kosta.ems.attendance;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collection;

@Service
public interface AttendanceService {
    int getNumberOfAttendance(LocalDate startDate, LocalDate endDate, String studentId);

    int getNumberOfLeave(LocalDate startDate, LocalDate endDate, String studentId);


    /* 훈련 수당 대상자 :  출석률 80%*/
    
    // [출결] - 수강생 출결 조회 목록 데이터 개수 (for 페이지네이션)
    int getStudentAttendanceListAmount(String name, int courseNumber);
    
    // [출결] - 수강생 출석 조회 목록 조회
    Collection<StudentAttendanceListDTO> getStudentAttendanceList(String name, int courseNumber, int page, int size);
    
    // [출결] - 선택한 수강생의 출석 상태 수정
    void updateStudentAttendance(String attendanceStatus, LocalDate attendanceDate, String studentId);
}
