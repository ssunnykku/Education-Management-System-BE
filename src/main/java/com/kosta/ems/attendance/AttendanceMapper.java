package com.kosta.ems.attendance;

import com.kosta.ems.attendance.dto.*;
import org.apache.ibatis.annotations.Mapper;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

@Mapper
public interface AttendanceMapper {
    Collection<AttendanceStudentCourseDTO> selectAttendanceByStudentIdAndDuration(String startDate, String endDate, String studentId);

    int selectCountAttendance(LocalDate startDate, LocalDate endDate, String studentId);

    int selectCountLeave(LocalDate startDate, LocalDate endDate, String studentId);

    /*
    // [출결 조회] - 수강생 출결 조회 목록 데이터 개수 (for 페이지네이션)
    // 경우1 _ 기수+수강생명 입력
    Collection<StudentAttendanceListDTO> selectAttendanceIntegratedListFilterAllAmount(String name, int courseNumber);
    // 경우2_ 기수 또는 수강생명 입력
    Collection<StudentAttendanceListDTO> selectAttendanceIntegratedListFilterAmount(String name, int courseNumber);
    // 경우3_ 기수, 수강생명 미입력 (전체 데이터)
    Collection<StudentAttendanceListDTO> selectAttendanceIntegratedListNoFilterAmount(String name, int courseNumber);

    // [출결 조회] - 수강생 출석 조회 목록 조회
    // 경우1 _ 기수+수강생명 입력
    List<StudentAttendanceListDTO> selectAttendanceIntegratedListFilterAll(String name, int courseNumber, int page, int size);
    // 경우2_ 기수 또는 수강생명 입력
    List<StudentAttendanceListDTO> selectAttendanceIntegratedListFilter(String name, int courseNumber, int page, int size);
    // 경우3_ 기수, 수강생명 미입력 (전체 데이터)
    List<StudentAttendanceListDTO> selectAttendanceIntegratedListNoFilter(String name, int courseNumber, int page, int size);
     */

    /*
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
    */


    // 2차 - 경우 1~3을 하나의 쿼리문으로 해결하기
    // int selectAttendanceIntegratedListAmount(String name, int courseNumber, String academyLocation);
    List<StudentAttendanceListDTO> selectAttendanceIntegratedListAmount(String name, int courseNumber, String academyLocation);

    List<StudentAttendanceListDTO> selectAttendanceIntegratedList(String name, int courseNumber, String academyLocation, int page, int size);


    // 테스트_sql if문
    // 검색 결과 데이터 목록 가져오기
    int selectAttendanceStatusListAmount(LocalDate attendanceDate, String academyLocation, String name, int courseNumber);

    List<AttendanceListBySearchFilterDTO> selectAttendanceStatusList(LocalDate attendanceDate, String academyLocation, String name, int courseNumber, int page, int size);


    // [출결 수정]
    // 선택한 수강생 출석 상태 수정
    int updateStudentAttendance(UpdateStudentAttendanceStatusDTO dto);

    // --[출석 인정]
    // --출석 인정 항목 리스트 가져오기
    List<AttendanceAcknowledgeDTO> selectAcknowledgeCategoryList(int isActive);

    // --출석 인정항목*인정일수 적용하여 출결 상태 반영
    int updateAttendanceAcknowledgeStatus(String attendanceStatus, String evidentialDocuments, int acknowledgeSeq, LocalDate attendanceDate, int studentCourseSeq);

    int insertAttendanceAcknowledgeStatus(LocalDate attendanceDate, int studentCourseSeq, String attendanceStatus, String managerId, String evidentialDocuments, int acknowledgeSeq);

    // [출결 등록]
    // 1. 특정일의 출결 상태가 등록되지 않은 수강생 목록 가져오기
    List<AttendanceListBySearchFilterDTO> selectNoAttendanceStatusStudentList(LocalDate attendanceDate, String academyLocation);

    // 2. 목록의 학생 중 선택한 학생의 출결 상태 등록하기
    int insertAttendanceStatus(UpdateStudentAttendanceStatusDTO dto);
}
