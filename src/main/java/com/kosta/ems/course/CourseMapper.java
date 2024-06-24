package com.kosta.ems.course;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CourseMapper {
	CourseDTO getCourse(@Param("courseNumber") int courseNumber);
    List<CourseDTO> searchCourseList(@Param("courseNumber") int courseNumber, @Param("academyLocation") String academyLocation, @Param("offset") int offset, @Param("pageSize") int pageSize);
    
    boolean insertCourse(CourseDTO course);
    boolean updateCourse(CourseDTO course);
    boolean inactivateCourse(int courseSeq);
}
