package com.kosta.ems.courses;

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
		if(course == null || !course.getAcademyLocation().equals(academyLocationOfManager))
			return null;
		return course;
	}

	@Override
	public List<CourseDTO> searchCourseList(int courseNumber, String academyLocation, int page, int pageSize, boolean excludeExpired) {
		return courseMapper.searchCourseList(courseNumber, academyLocation, (page-1) * pageSize, pageSize, excludeExpired);
	}
	
	@Override
	public Integer getSearchCourseListSize(int courseNumber, String academyLocation, int page, int pageSize, boolean excludeExpired) {
		Integer result =courseMapper.getSearchCourseListSize(courseNumber, academyLocation, (page-1) * pageSize, pageSize, excludeExpired);
		if(result == null) {
			result = 0;
		}
		return result;
	}

	@Override
	public boolean addCourse(CourseDTO course) {
		return courseMapper.insertCourse(course);
	}

	@Override
	public boolean editCourse(CourseDTO course) {
		CourseDTO ori = courseMapper.getCourse(course.getCourseSeq()); 
		if(course.getAcademyLocation().equals(ori.getAcademyLocation()))
			return courseMapper.updateCourse(course);
		//TODO:보안 런타임 오류 
		return false;
	}

	@Override
	public boolean deleteCourse(int courseSeq, String academyLocationOfManager) {
		// 삭제권한 체크목적
		CourseDTO course = getCourse(courseSeq, academyLocationOfManager);
		if(course == null) {
			return false;
		}
		return courseMapper.inactivateCourse(courseSeq);
	}

	@Override
	public List<Integer> getCourseNumberList(String academyLocation, boolean excludeExpired) {
		return courseMapper.getCourseNumberList(academyLocation, excludeExpired);
	}

}
