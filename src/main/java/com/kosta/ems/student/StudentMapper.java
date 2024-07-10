package com.kosta.ems.student;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;

import com.kosta.ems.attendance.StudentAttendanceListDTO;
import com.kosta.ems.attendance.UpdateStudentAttendanceStatusDTO;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

@Mapper
public interface StudentMapper {
    Collection<StudentCourseInfoDTO> selectStudentByName(@Param("name") String name);

    // 수강생 정보 조회
    int findByStudentNumberOrCourseNumberAll(String name, int courseNumber);

    List<StudentBasicInfoDTO> findByStudentNameOrCourseNumberList(@Param("name") String name, @Param("courseNumber") int courseNumber, int page, int size);

    // *0710_수강생 정보 조회
    int selectStudentInfoListCnt(int isActive, String name, int courseNumber);
    List<StudentInfoDTO> selectStudentInfoList(int isActive, String name, int courseNumber, int page, int size);
    // *0710_수강생 정보 조회 (end)

    // *0710_수강생 id로 수강내역 조회
    List<StudentCourseHistoryDTO> selectStudentCourseHistory(String studentId);

    // 수강생 등록
    int findByHrdNetId(String hrdNetId);

    RegisteredStudentInfoDTO selectRegisteredStudentBasicInfo(String hrdNetId);

    // 현재 진행 중+등록 가능한 교육과정 목록
    List<CourseInfoDTO> selectOnGoingCourseList(String academyLocation);

    // void addStudentBasicInfo(AddStudentBasicInfoDTO dto);
    int addStudentBasicInfo(AddStudentBasicInfoDTO dto);

    int addStudentCourseSeqInfo(AddStudentBasicInfoDTO dto);

    // 수강생 정보 수정
    StudentBasicInfoDTO selectRegisteredStudentInfo(String studentId);
    int updateSelectedStudentInfo(UpdateSelectedStudentInfoDTO dto);

    // 수강생 삭제 (isActive 업데이트)
    void deleteSelectedStudent(String studentId);

    String selectAddressByStudentId(String studentId);

    // scq로 수강생 기본 정보 가져오기
    GetStudentInfoByScqDTO selectStudentInfoByScq(int studentCourseSeq);

    List<StudentCourseInfoDTO> selectStudentListBycourseSeq(int courseSeq);

}