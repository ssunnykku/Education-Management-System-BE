package com.kosta.ems.courses;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CourseService {
	CourseDTO getCourse(int courseSeq, String academyLocation);
    List<CourseDTO> searchCourseList(int courseNumber, String academyLocation, int page, int pageSize);
    
    boolean addCourse(CourseDTO course);
    boolean editCourse(CourseDTO course, String academyLocation);
    boolean deleteCourse(int courseSeq, String academyLocationOfManager);
}
