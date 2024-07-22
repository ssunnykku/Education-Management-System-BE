package com.kosta.ems.course;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {
    private final CourseMapper courseMapper;

    @Override
    public CourseDTO getCourse(int courseSeq, String academyLocationOfManager) {
        CourseDTO course = courseMapper.getCourse(courseSeq);
        //권한 검사
        if (course == null || !course.getAcademyLocation().equals(academyLocationOfManager))
            return null;
        return course;
    }

    @Override
    public List<CourseDTO> searchCourseList(int courseNumber, String academyLocation, int page, int pageSize, boolean excludeExpired) {
        return courseMapper.searchCourseList(courseNumber, academyLocation, (page - 1) * pageSize, pageSize, excludeExpired);
    }

    @Override
    public Integer getSearchCourseListSize(int courseNumber, String academyLocation, int page, int pageSize, boolean excludeExpired) {
        Integer result = courseMapper.getSearchCourseListSize(courseNumber, academyLocation, (page - 1) * pageSize, pageSize, excludeExpired);
        if (result == null) {
            result = 0;
        }
        return result;
    }

    @Override
    public boolean addCourse(CourseDTO course) {
        boolean result = false;
        result = courseMapper.insertCourse(course);
        return result;
    }

    @Override
    public boolean editCourse(CourseDTO course) {
        CourseDTO ori = courseMapper.getCourse(course.getCourseSeq());
        if (course.getAcademyLocation().equals(ori.getAcademyLocation()))
            return courseMapper.updateCourse(course);
        //TODO:보안 런타임 오류
        return false;
    }

    @Override
    public boolean deleteCourse(int courseSeq, String academyLocationOfManager) {
        // 삭제권한 체크목적
        CourseDTO course = courseMapper.getCourse(courseSeq);
        if (course.getAcademyLocation().equals(academyLocationOfManager)) {
            return courseMapper.inactivateCourse(courseSeq);
        }
        return false;
    }

    @Override
    public List<Integer> getCourseNumberList(String academyLocation, boolean excludeExpired) {
        return courseMapper.getCourseNumberList(academyLocation, excludeExpired);
    }

    @Override
    public List<Integer> getCourseNumberByYear(int courseEndYear) {
        return courseMapper.getCourseNumberListByYear(courseEndYear);
    }

    @Override
    public List<Integer> getCourseNumberYearList() {
        return courseMapper.getCourseNumberYearList();
    }

    @Override
    public Integer getStudentsNumberBySeq(int courseSeq) {
        Integer result = courseMapper.getStudentsNumberBySeq(courseSeq);
        if (result == null) {
            result = 0;
        }
        return result;
    }

    @Override
    public Integer getSeqByCourseNumber(int courseNumber) {
        return courseMapper.getSeqByCourseNumber(courseNumber);
    }


    @Override
    public CourseDTO getCourseByCourseNumber(int CourseNumber) {
        return courseMapper.getCourseByCourseNumber(CourseNumber);
    }

    @Override
    public List<CourseDTO> getCurrentCourseList(LocalDate currentDate, String academyLocation) {
        return courseMapper.getCurrentCourseList(currentDate, academyLocation);
    }

    @Override
    public List<String> getCourseTypeList() {
        return courseMapper.getCourseTypeList();
    }
    @Override
    public String getCourseNameByCourseNumber(int courseNumber) {
    	return courseMapper.getCourseNameByCourseNumber(courseNumber);
    }

}
