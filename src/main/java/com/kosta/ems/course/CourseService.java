package com.kosta.ems.course;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

public interface CourseService {
    List<Integer> getCourseNumberList(String academyLocation, boolean excludeExpired);

    CourseDTO getCourse(int courseSeq, String academyLocation);

    List<CourseDTO> searchCourseList(int courseNumber, String academyLocation, int page, int pageSize, boolean excludeExpired);

    Integer getSearchCourseListSize(int courseNumber, String academyLocation, int page, int pageSize, boolean excludeExpired);

    List<String> getCourseTypeList();

    CourseDTO getCourseByCourseNumber(int CourseNumber);

    boolean addCourse(CourseDTO course);

    boolean editCourse(CourseDTO course);

    boolean deleteCourse(int courseSeq, String academyLocationOfManager);

    List<CourseDTO> getCurrentCourseList(LocalDate currentDate, String academyLocation);

    List<Integer> getCourseNumberByYear(int courseEndYear);

    List<Integer> getCourseNumberYearList();

    Integer getStudentsNumberBySeq(int courseSeq);

    Integer getSeqByCourseNumber(int courseNumber);
    
    String getCourseNameByCourseNumber(int courseNumber);
}
