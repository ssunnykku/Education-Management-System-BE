package com.kosta.ems.attendance;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Collection;


@Log4j2
@Service
@RequiredArgsConstructor
@Transactional
public class AttendanceServiceImpl implements AttendanceService {

    private final AttendanceMapper attendanceMapper;

    @Override
    public int getNumberOfAttendance(LocalDate startDate, LocalDate endDate, String studentId) {
        return attendanceMapper.selectCountAttendance(startDate, endDate, studentId);
    }

    @Override
    public int getNumberOfLeave(LocalDate startDate, LocalDate endDate, String studentId) {
        return attendanceMapper.selectCountLeave(startDate, endDate, studentId);
    }
    
    // [출결] - 수강생 출결 조회 목록 데이터 개수 (for 페이지네이션)
    @Override
    public int getStudentAttendanceListAmount(String name, int courseNumber) {
    	return attendanceMapper.selectStudentAttendanceListAmount(name, courseNumber).size();
    }
    
    // [출결] - 수강생 출석 조회 목록 조회
    @Override
    public Collection<StudentAttendanceListDTO> getStudentAttendanceList(String name, int courseNumber, int page, int size) {
    	return attendanceMapper.selectStudentAttendanceList(name, courseNumber, page, size);
    }
    
    // [출결] - 선택한 수강생의 출석 상태 수정
    @Override
    public void updateStudentAttendance(String attendanceStatus, String attendanceDate, String studentId) {
    	int year = Integer.parseInt(attendanceDate.split("-")[0]);
        int month = Integer.parseInt(attendanceDate.split("-")[1]);
        int day = Integer.parseInt(attendanceDate.split("-")[2]);
    	UpdateStudentAttendanceStatusDTO dto = UpdateStudentAttendanceStatusDTO.builder().attendanceStatus(attendanceStatus).attendanceDate(LocalDate.of(year, month, day)).studentId(studentId).build();
    	attendanceMapper.updateStudentAttendance(dto);
    }
}
