package com.kosta.ems.course;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;

@Mapper
public interface CourseMapper {
    List<Integer> getCourseNumberList(@Param("academyLocation") String academyLocation, @Param("excludeExpired") boolean excludeExpired);

    CourseDTO getCourse(@Param("courseSeq") int courseSeq);

    List<CourseDTO> searchCourseList(@Param("courseNumber") int courseNumber, @Param("academyLocation") String academyLocation, @Param("offset") int offset, @Param("pageSize") int pageSize, @Param("excludeExpired") boolean excludeExpired);

    Integer getSearchCourseListSize(@Param("courseNumber") int courseNumber, @Param("academyLocation") String academyLocation, @Param("offset") int offset, @Param("pageSize") int pageSize, @Param("excludeExpired") boolean excludeExpired);

    List<String> getCourseTypeList();

    boolean insertCourse(CourseDTO course);

    boolean updateCourse(CourseDTO course);

    boolean inactivateCourse(int courseSeq);

    CourseDTO getCourseByCourseNumber(int courseNumber);

    List<CourseDTO> getCurrentCourseList(LocalDate currentDate, String academyLocation);

    List<Integer> getCourseNumberListByYear(@Param("courseEndYear") int courseEndYear);

    List<Integer> getCourseNumberYearList();

    Integer getStudentsNumberBySeq(@Param("courseSeq") int courseSeq);

    Integer getSeqByCourseNumber(@Param("courseNumber") int courseNumber);
    
    String getCourseNameByCourseNumber(@Param("courseNumber") int courseNumber);
}
