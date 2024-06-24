package com.kosta.ems.course;

import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {
	private final CourseMapper courseMapper;

	@Override
	public CourseDTO getCourse(int courseNumber, String academyLocation) {
		CourseDTO course = courseMapper.getCourse(courseNumber);
		//권한 검사
		if(Objects.nonNull(course) && !course.getAcademyLocation().equals(academyLocation))
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
	public boolean editCourse(CourseDTO course) {
		return courseMapper.updateCourse(course);
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
