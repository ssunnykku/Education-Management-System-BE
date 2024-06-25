package com.kosta.ems.student;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@Service
public interface StudentService {
	
    Map<String, Collection> getStudentByName(String name);
    // Collection<StudentBasicInfoDTO> getStudentsByNameOrCourseNumber(String name, int courseNumber);
    // Map<String, Collection> getStudentsByNameOrCourseNumber(String name, int courseNumber);
    // List<StudentBasicInfoDTO> getStudentsByNameOrCourseNumber(String name, int courseNumber);
    int getStudentsByNameOrCourseNumberAmount(String name, String courseNumber);
    List<StudentBasicInfoDTO> getStudentsByNameOrCourseNumber(String name, int courseNumber, int page, int size);
    // List<StudentBasicInfoDTO> getStudentsByNameOrCourseNumber(String name, int courseNumber, Pageable);

    // 수강생 등록
    boolean findByHrdNetId(String hrdNetId);
    RegisteredStudentInfoDTO getRegisteredStudentBasicInfo(String hrdNetId);
    void addStudentBasicInfo(String hrdNetId, String name, String birth, String address, String bank, String account, String phoneNumber, String email, String gender, String managerId);
    void addStudentCourseSeqInfo(String hrdNetId, String courseNumber);

    // 수강생 정보 수정
    void updateSelectedStudentInfo(String name, String address, String bank, String account, String phoneNumber, String email, String studentId);
    
    // 수강생 삭제(isActive 값 수정)
    void deleteSelectedStudent(String studentId);
    
    // [출결] - 수강생 출석 조회 목록 조회
    Collection<StudentAttendanceListDTO> getStudentAttendanceList(String name, String courseNumber);
    
    // [출결] - 선택한 수강생의 출석 상태 수정
    void updateStudentAttendance(String attendanceStatus, String attendanceDate, String studentId);
}