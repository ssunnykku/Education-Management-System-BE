package com.kosta.ems.student;

import com.kosta.ems.student.StudentCourseInfoDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.Collection;
import java.util.List;

@Mapper
public interface StudentMapper {
    Collection<StudentCourseInfoDTO> selectStudentByName(@Param("name") String name);
    List<StudentBasicInfoDTO> findByStudentNameOrCourseNumber(@Param("name") String name, @Param("courseNumber") int courseNumber, int start, int size);
}
