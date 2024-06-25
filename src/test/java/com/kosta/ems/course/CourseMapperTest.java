package com.kosta.ems.course;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.kosta.ems.courses.CourseDTO;
import com.kosta.ems.courses.CourseMapper;

import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@Slf4j
public class CourseMapperTest {
	@Autowired
	CourseMapper mapper;

	@Test
	@Transactional
	public void GetCoursesList() {
		List<CourseDTO> list = mapper.searchCourseList(277, "가산", 0, 10, false);
		assertThat(list.get(0).getCourseSeq()).isEqualTo(19);
		list = mapper.searchCourseList(0, "가산", 0, 10, false);
		assertThat(list.size()).isGreaterThan(1);
	}
	
	@Test
	@Transactional
	public void GetCourse() {
		CourseDTO course = mapper.getCourse(27);
		System.out.println(course.toString());
		assertThat(course.getCourseSeq()).isEqualTo(27);
	}


	@Test
	@Transactional
	public void AddCourse() {
		CourseDTO course = 
				CourseDTO.builder()
				.managerId("d893c34e-2f8f-11ef-b0b2-0206f94be675")
				.courseNumber(3000)
				.academyLocation("가산")
				.courseName("123")
				.courseStartDate(LocalDate.now())
				.courseEndDate(LocalDate.now())
				.subject("자바")
				.courseType("구직자")
				.totalTrainingDays(100)
				.trainingHoursOfDate(8)
				.professorName("정")
				.maxStudents(100)
				.build();
		assertThat(mapper.insertCourse(course)).isTrue();
	}
	
	@Test
	@Transactional
	public void updateCourse() {
		CourseDTO course = 
				CourseDTO.builder()
				.managerId("d893c34e-2f8f-11ef-b0b2-0206f94be675")
				.courseNumber(3000)
				.academyLocation("가산")
				.courseName("123")
				.courseStartDate(LocalDate.now())
				.courseEndDate(LocalDate.now())
				.subject("자바")
				.courseType("구직자")
				.totalTrainingDays(100)
				.trainingHoursOfDate(8)
				.professorName("정")
				.maxStudents(100)
				.build();
		mapper.insertCourse(course);
		course = mapper.searchCourseList(3000, "가산", 0, 10, false).get(0);
		course.setCourseName("수정된 이름");
		assertThat(mapper.updateCourse(course)).isTrue();
	}
	
	@Test
	@Transactional
	public void inactivateCourse() {
		CourseDTO course = 
				CourseDTO.builder()
				.managerId("d893c34e-2f8f-11ef-b0b2-0206f94be675")
				.courseNumber(3000)
				.academyLocation("가산")
				.courseName("123")
				.courseStartDate(LocalDate.now())
				.courseEndDate(LocalDate.now())
				.subject("자바")
				.courseType("구직자")
				.totalTrainingDays(100)
				.trainingHoursOfDate(8)
				.professorName("정")
				.maxStudents(100)
				.build();
		mapper.insertCourse(course);
		int seq = mapper.searchCourseList(3000, "가산", 0, 10, false).get(0).getCourseSeq();
		assertThat(mapper.inactivateCourse(seq)).isTrue();
	}
	
	

}
