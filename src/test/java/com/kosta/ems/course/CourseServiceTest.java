package com.kosta.ems.course;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.kosta.ems.courses.CourseDTO;
import com.kosta.ems.courses.CourseService;

@SpringBootTest
public class CourseServiceTest {
	@Autowired
	CourseService service;
	
	@Test
	@Transactional
	public void getCourse() {
		assertThat(service.getCourse(27, "가산")).isNotNull();
		assertThat(service.getCourse(27, "강남")).isNull();
	}
	
	@Test
	@Transactional
    public void searchCourseList() {
		assertThat(service.searchCourseList(277, "가산", 1, 10).size()).isEqualTo(1);
		assertThat(service.searchCourseList(0, "가산", 1, 10).size()).isGreaterThan(2);
	}
    
	@Test
	@Transactional
	public void addCourse() {
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
		assertThat(service.addCourse(course));
	}
//	@Test
	@Transactional
	public void editCourse() {
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
		service.addCourse(course);
		course = service.getCourse(3000, "가산");
		course.setAcademyLocation("강남");
		assertThat(service.editCourse(course)).isTrue();
	}
	@Test
	@Transactional
    public void deleteCourse() {
		assertThat(service.deleteCourse(27, "가산")).isTrue();
	}
	
	
}
