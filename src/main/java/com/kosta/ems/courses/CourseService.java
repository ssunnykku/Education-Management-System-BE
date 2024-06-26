package com.kosta.ems.courses;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CourseService {
	List<Integer> getCourseNumberList(String academyLocation, boolean excludeExpired);
	CourseDTO getCourse(int courseSeq, String academyLocation);
    List<CourseDTO> searchCourseList(int courseNumber, String academyLocation, int page, int pageSize, boolean excludeExpired);
    int getsearchCourseListSize(int courseNumber, String academyLocation, int page, int pageSize, boolean excludeExpired);
    
    boolean addCourse(CourseDTO course);
    boolean editCourse(CourseDTO course);
    boolean deleteCourse(int courseSeq, String academyLocationOfManager);
}
