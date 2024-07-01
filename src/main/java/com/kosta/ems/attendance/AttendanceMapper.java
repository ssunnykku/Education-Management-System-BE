package com.kosta.ems.attendance;

import org.apache.ibatis.annotations.Mapper;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

@Mapper
public interface AttendanceMapper {
    Collection<AttendanceStudentCourseDTO> selectAttendanceByStudentIdAndDuration(String startDate, String endDate, String studentId);

    int selectCountAttendance(LocalDate startDate, LocalDate endDate, String studentId);

    int selectCountLeave(LocalDate startDate, LocalDate endDate, String studentId);
    
    // [출결 조회] - 수강생 출결 조회 목록 데이터 개수 (for 페이지네이션)
    // 경우1 _ 기수+수강생명 입력
    Collection<StudentAttendanceListDTO> selectAttendanceIntegratedListFilterAllAmount(String name, int courseNumber);
    // 경우2_ 기수 또는 수강생명 입력
    Collection<StudentAttendanceListDTO> selectAttendanceIntegratedListFilterAmount(String name, int courseNumber);
    // 경우3_ 기수, 수강생명 미입력 (전체 데이터)
    Collection<StudentAttendanceListDTO> selectAttendanceIntegratedListNoFilterAmount(String name, int courseNumber);

    // [출결 조회] - 수강생 출석 조회 목록 조회
    // 경우1 _ 기수+수강생명 입력
    // Collection<StudentAttendanceListDTO> selectAttendanceIntegratedListFilterAll(String name, int courseNumber, int page, int size);
    List<StudentAttendanceListDTO> selectAttendanceIntegratedListFilterAll(String name, int courseNumber, int page, int size);
    // 경우2_ 기수 또는 수강생명 입력
    List<StudentAttendanceListDTO> selectAttendanceIntegratedListFilter(String name, int courseNumber, int page, int size);
    // 경우3_ 기수, 수강생명 미입력 (전체 데이터)
    List<StudentAttendanceListDTO> selectAttendanceIntegratedListNoFilter(String name, int courseNumber, int page, int size);


    // [출결 수정] - 특정일의 수강생 출석 상태 목록 조회 (for 출결 입력/수정)
    // 경우1 _ 기수+수강생명 입력
    // 검색 결과 개수 가져오기 (for 페이지네이션)
    int selectCourseNumberAndStudentNameListAmount(LocalDate attendanceDate, String academyLocation, String name, int courseNumber);
    // 검색 결과 데이터 목록 가져오기
    List<AttendanceListBySearchFilterDTO> selectCourseNumberAndStudentNameList(LocalDate attendanceDate, String academyLocation, String name, int courseNumber, int page, int size);
    
    // 경우2 _ 기수 또는 수강생명 입력
    // 검색 결과 개수 가져오기 (for 페이지네이션)
    int selectCourseNumberOrStudentNameListAmount(LocalDate attendanceDate, String academyLocation, String name, int courseNumber);
    // 검색 결과 데이터 목록 가져오기
    List<AttendanceListBySearchFilterDTO> selectCourseNumberOrStudentNameList(LocalDate attendanceDate, String academyLocation, String name, int courseNumber, int page, int size);
    
    // 경우3 _ 기수+수강생명 미입력
    // 검색 결과 개수 가져오기 (for 페이지네이션)
    int selectDateAndLocationListAmount(LocalDate attendanceDate, String academyLocation, String name, int courseNumber);
    // 검색 결과 데이터 목록 가져오기
    List<AttendanceListBySearchFilterDTO> selectDateAndLocationList(LocalDate attendanceDate, String academyLocation, String name, int courseNumber, int page, int size);
    
    // [출결] - 선택한 수강생 출석 상태 수정
    // void updateStudentAttendance(UpdateStudentAttendanceStatusDTO dto);
    int updateStudentAttendance(UpdateStudentAttendanceStatusDTO dto);
}
