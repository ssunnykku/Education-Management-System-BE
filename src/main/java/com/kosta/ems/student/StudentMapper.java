package com.kosta.ems.student;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;

import com.kosta.ems.attendance.StudentAttendanceListDTO;
import com.kosta.ems.attendance.UpdateStudentAttendanceStatusDTO;

import java.util.Collection;
import java.util.List;

@Mapper
public interface StudentMapper {
    Collection<StudentCourseInfoDTO> selectStudentByName(@Param("name") String name);
    // 수강생 정보 조회
    int findByStudentNumberOrCourseNumberAll(@Param("name") String name, @Param("courseNumber") int courseNumber);
    List<StudentBasicInfoDTO> findByStudentNameOrCourseNumberList(@Param("name") String name, @Param("courseNumber") int courseNumber, int start, int size);
    // 수강생 등록
    int findByHrdNetId(String hrdNetId);
    RegisteredStudentInfoDTO selectRegisteredStudentBasicInfo(String hrdNetId);
    void addStudentBasicInfo(AddStudentBasicInfoDTO dto);
    void addStudentCourseSeqInfo(AddStudentCourseSeqDTO dto);
    // 수강생 정보 수정
    void updateSelectedStudentInfo(UpdateSelectedStudentInfoDTO dto, String studentId); 
    // 수강생 삭제 (isActive 업데이트)
    void deleteSelectedStudent(String studentId);
}