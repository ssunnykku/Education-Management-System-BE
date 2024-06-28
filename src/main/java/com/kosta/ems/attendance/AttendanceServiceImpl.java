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

    // [출결] - 수강생 출석 조회 목록 조회
    // 경우1 _ 기수+수강생명 입력
    // -- 데이터 개수 가져오기 (for 페이지네이션)
    @Override
    public int getAttendanceIntegratedListFilterAllAmount(String name, int courseNumber) {
        return attendanceMapper.selectAttendanceIntegratedListFilterAllAmount(name, courseNumber).size();
    }
    // -- 데이터 결과 목록 가져오기
    @Override
    public Collection<StudentAttendanceListDTO> getAttendanceIntegratedListFilterAll(String name, int courseNumber, int page, int size) {
        return attendanceMapper.selectAttendanceIntegratedListFilterAll(name, courseNumber, page, size);
    }
    // 경우2_ 기수 또는 수강생명 입력
    // -- 데이터 개수 가져오기 (for 페이지네이션)
    @Override
    public int getAttendanceIntegratedListFilterAmount(String name, int courseNumber) {
        return attendanceMapper.selectAttendanceIntegratedListFilterAmount(name, courseNumber).size();
    }
    // -- 데이터 결과 목록 가져오기
    @Override
    public Collection<StudentAttendanceListDTO> getAttendanceIntegratedListFilter(String name, int courseNumber, int page, int size) {
        return attendanceMapper.selectAttendanceIntegratedListFilter(name, courseNumber, page, size);
    }

    /*
    // [출결] - 수강생 출석 조회 목록 조회
    @Override
    public Collection<StudentAttendanceListDTO> getStudentAttendanceList(String name, int courseNumber, int page, int size) {
    	return attendanceMapper.selectAttendanceIntegratedListFilterAll(name, courseNumber, page, size);
    }
    */
    
    // [출결] - 특정일의 수강생 출석 상태 목록 조회 (for 출결 입력/수정)
    // 경우1 _ 기수+수강생명 입력
    // 검색 결과 개수 가져오기 (for 페이지네이션)
    @Override
    public int selectCourseNumberAndStudentNameListAmount(String attendanceDate, String academyLocation, String name, int courseNumber) {
    	int year = Integer.parseInt(attendanceDate.split("-")[0] );
    	int month = Integer.parseInt(attendanceDate.split("-")[1]);
    	int day = Integer.parseInt(attendanceDate.split("-")[2]);
    	
    	return attendanceMapper.selectCourseNumberAndStudentNameListAmount(LocalDate.of(year, month, day), academyLocation, name, courseNumber);
    }
    // 검색 결과 데이터 목록 가져오기
    @Override
    public Collection<AttendanceListBySearchFilterDTO> selectCourseNumberAndStudentNameList(String attendanceDate, String academyLocation, String name, int courseNumber, int page, int size) {
    	int year = Integer.parseInt(attendanceDate.split("-")[0] );
    	int month = Integer.parseInt(attendanceDate.split("-")[1]);
    	int day = Integer.parseInt(attendanceDate.split("-")[2]);
    	
    	return attendanceMapper.selectCourseNumberAndStudentNameList(LocalDate.of(year, month, day), academyLocation, name, courseNumber, page, size);
    }
    
    // 경우2 _ 기수 또는 수강생명 입력
    // 검색 결과 개수 가져오기 (for 페이지네이션)
    @Override
    public int selectCourseNumberOrStudentNameListAmount(String attendanceDate, String academyLocation, String name, int courseNumber) {
    	int year = Integer.parseInt(attendanceDate.split("-")[0] );
    	int month = Integer.parseInt(attendanceDate.split("-")[1]);
    	int day = Integer.parseInt(attendanceDate.split("-")[2]);
    	
    	return attendanceMapper.selectCourseNumberOrStudentNameListAmount(LocalDate.of(year, month, day), academyLocation, name, courseNumber);
    }
    // 검색 결과 데이터 목록 가져오기
    @Override
    public Collection<AttendanceListBySearchFilterDTO> selectCourseNumberOrStudentNameList(String attendanceDate, String academyLocation, String name, int courseNumber, int page, int size) {
    	int year = Integer.parseInt(attendanceDate.split("-")[0] );
    	int month = Integer.parseInt(attendanceDate.split("-")[1]);
    	int day = Integer.parseInt(attendanceDate.split("-")[2]);
    	
    	return attendanceMapper.selectCourseNumberOrStudentNameList(LocalDate.of(year, month, day), academyLocation, name, courseNumber, page, size);
    }
    
    // 경우3 _ 기수+수강생명 미입력
    // 검색 결과 개수 가져오기 (for 페이지네이션)
    @Override 
    public int selectDateAndLocationListAmount(String attendanceDate, String academyLocation, String name, int courseNumber) {
    	int year = Integer.parseInt(attendanceDate.split("-")[0] );
    	int month = Integer.parseInt(attendanceDate.split("-")[1]);
    	int day = Integer.parseInt(attendanceDate.split("-")[2]);
    	
    	return attendanceMapper.selectDateAndLocationListAmount(LocalDate.of(year, month, day), academyLocation, name, courseNumber);
    }
    // 검색 결과 데이터 목록 가져오기
    @Override
    public Collection<AttendanceListBySearchFilterDTO> selectDateAndLocationList(String attendanceDate, String academyLocation, String name, int courseNumber, int page, int size) {
    	int year = Integer.parseInt(attendanceDate.split("-")[0] );
    	int month = Integer.parseInt(attendanceDate.split("-")[1]);
    	int day = Integer.parseInt(attendanceDate.split("-")[2]);
    	
    	return attendanceMapper.selectDateAndLocationList(LocalDate.of(year, month, day), academyLocation, name, courseNumber, page, size);
    }
    
    
    // [출결] - 선택한 수강생의 출석 상태 수정
    @Override
    public void updateStudentAttendance(String attendanceStatus, String attendanceDate, String studentId) {
    	
    	int year = Integer.parseInt(attendanceDate.split("-")[0]);
        int month = Integer.parseInt(attendanceDate.split("-")[1]);
        int day = Integer.parseInt(attendanceDate.split("-")[2]);
        
    	UpdateStudentAttendanceStatusDTO dto = UpdateStudentAttendanceStatusDTO.builder().attendanceStatus(attendanceStatus).attendanceDate(LocalDate.of(year, month, day)).studentId(studentId).build();
    	// UpdateStudentAttendanceStatusDTO dto = UpdateStudentAttendanceStatusDTO.builder().attendanceStatus(attendanceStatus).attendanceDate(attendanceDate).studentId(studentId).build();
    	attendanceMapper.updateStudentAttendance(dto);
    }
}
