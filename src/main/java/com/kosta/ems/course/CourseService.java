package com.kosta.ems.course;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CourseService {
	CourseDTO getCourse(int courseNumber, String academyLocation);
    List<CourseDTO> searchCourseList(int courseNumber, String academyLocation, int page, int pageSize);
    
    boolean addCourse(CourseDTO course);
    boolean editCourse(CourseDTO course);
    boolean deleteCourse(int courseSeq, String academyLocationOfManager);
}
