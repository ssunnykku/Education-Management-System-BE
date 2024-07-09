package com.kosta.ems.student;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kosta.ems.attendance.StudentAttendanceListDTO;

import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Service
public interface StudentService {
	
    Map<String, Collection> getStudentByName(String name);
    
    // 수강생 검색 결과 총 개수
    int getStudentsByNameOrCourseNumberAmount(String name, int courseNumber);
    
    // 수강생 검색 결과 목록
    List<StudentBasicInfoDTO> getStudentsByNameOrCourseNumberList(String name, int courseNumber, int page, int size);

    // 수강생 등록
    boolean findByHrdNetId(String hrdNetId);
    RegisteredStudentInfoDTO getRegisteredStudentBasicInfo(String hrdNetId);
    List<CourseInfoDTO> getOnGoingCourseList(String academyLocation);
    // 신규 수강생 등록
    void setStudentWithCourse(String hrdNetId, String name, String birth, String address, String bank, String account, String phoneNumber, String email, String gender, String managerId, String courseNumber);
    void setStudentBasicInfo(String hrdNetId, String name, String birth, String address, String bank, String account, String phoneNumber, String email, String gender, String managerId, String courseNumber);
    void setStudentCourseSeqInfo(String hrdNetId, String courseNumber);

    // 수강생 정보 수정
    StudentBasicInfoDTO getRegisteredStudentInfo(String studentId);

    void updateSelectedStudentInfo(String name, String address, String bank, String account, String phoneNumber, String email, String studentId);
    
    // 수강생 삭제(isActive 값 수정)
    void removeSelectedStudent(String studentId);
    
}