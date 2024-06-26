package com.kosta.ems.courses;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CourseMapper {
	List<Integer> getCourseNumberList(@Param("academyLocation") String academyLocation, @Param("excludeExpired") boolean excludeExpired);
	CourseDTO getCourse(@Param("courseSeq") int courseSeq);
    List<CourseDTO> searchCourseList(@Param("courseNumber") int courseNumber, @Param("academyLocation") String academyLocation, @Param("offset") int offset, @Param("pageSize") int pageSize, @Param("excludeExpired") boolean excludeExpired);
    Integer getSearchCourseListSize( @Param("courseNumber") int courseNumber, @Param("academyLocation") String academyLocation, @Param("offset") int offset, @Param("pageSize") int pageSize, @Param("excludeExpired") boolean excludeExpired);
    
    boolean insertCourse(CourseDTO course);
    boolean updateCourse(CourseDTO course);
    boolean inactivateCourse(int courseSeq);
}
