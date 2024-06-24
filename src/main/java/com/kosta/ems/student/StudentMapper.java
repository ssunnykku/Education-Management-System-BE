package com.kosta.ems.student;

import com.kosta.ems.student.StudentCourseInfoDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;

@Mapper
public interface StudentMapper {
    Collection<StudentCourseInfoDTO> selectStudentByName(@Param("name") String name);
}
