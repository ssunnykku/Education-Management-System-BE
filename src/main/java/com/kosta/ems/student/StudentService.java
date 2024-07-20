package com.kosta.ems.student;

import com.kosta.ems.student.dto.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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

    // *0710_수강생 정보 조회
    int getStudentInfoListCnt(int isActive, String name, int courseNumber, String academyLocation);

    List<StudentInfoDTO> getStudentInfoList(int isActive, String name, int courseNumber, String academyLocation, int page, int size);

    // *0710_수강생 정보 조회 (end)
    List<ArrayList> getStudentInfoList2(int isActive, String name, int courseNumber, String academyLocation, int page, int size);

    // *0710_수강생 id로 수강내역 조회
    List<StudentCourseHistoryDTO> getStudentCourseHistory(String studentId);

    // 수강생 등록
    boolean findByHrdNetId(String hrdNetId);

    RegisteredStudentInfoDTO getRegisteredStudentBasicInfo(String hrdNetId);

    List<CourseInfoDTO> getOnGoingCourseList(String academyLocation);

    // 수강생 교육과정 수강신청(등록)
    void setStudentCourseSeqInfo(String hrdNetId, String courseNumber, String managerId);

    // 수강생 정보 수정
    StudentBasicInfoDTO getRegisteredStudentInfo(String studentId);

    void updateSelectedStudentInfo(String name, String address, String bank, String account, String phoneNumber, String email, String studentId, int isActiveStatus);

    // 수강생 삭제(isActive 값 수정)
    void removeSelectedStudent(String studentId);

}