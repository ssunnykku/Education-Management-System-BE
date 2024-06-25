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
	public CourseDTO getCourse(int courseSeq, String academyLocation) {
		CourseDTO course = courseMapper.getCourse(courseSeq);
		//권한 검사
		if(course == null || !course.getAcademyLocation().equals(academyLocation))
			return null;
		return course;
	}

	@Override
	public List<CourseDTO> searchCourseList(int courseNumber, String academyLocation, int page, int pageSize) {
		return courseMapper.searchCourseList(courseNumber, academyLocation, (page-1) * pageSize, pageSize);
	}

	@Override
	public boolean addCourse(CourseDTO course) {
		return courseMapper.insertCourse(course);
	}

	@Override
	public boolean editCourse(CourseDTO course, String academyLocationOfManager) {
		if(course.getAcademyLocation().equals(academyLocationOfManager))
			return courseMapper.updateCourse(course);
		//보안 런타임 오류
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

}
