package com.kosta.ems.course;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.kosta.ems.course.CourseDTO;
import com.kosta.ems.course.CourseMapper;

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
		assertThat(list.get(0).getCourseSeq()).isEqualTo(5);
		list = mapper.searchCourseList(0, "가산", 0, 10, false);
		assertThat(list.size()).isGreaterThan(1);
	}
	
	@Test
	@Transactional
	public void GetCourse() {
		CourseDTO course = mapper.getCourse(5);
		assertThat(course.getCourseSeq()).isEqualTo(5);
	}

	@Test
	@Transactional
	public void getCourseByCourseNumber() {
	    CourseDTO course= mapper.getCourseByCourseNumber(277);
	    assertThat(course.getCourseSeq()).isEqualTo(5);
	}
	
	@Test
	@Transactional
	public void searchCourseList() {
	    List<CourseDTO> list = mapper.searchCourseList(277, "가산", 0, 10, false);
	    assertThat(list.size()).isEqualTo(1);
	    list = mapper.searchCourseList(0, "가산", 0, 20, false);
	    assertThat(list.size()).isEqualTo(9);
	}
	
	@Test
	@Transactional
	public void getSearchCourseListSize() {
	    int count = mapper.getSearchCourseListSize(0, "가산", 0, 20, false);
	    assertThat(count).isEqualTo(9);
	}
	
	@Test
	@Transactional
	public void getCourseTypeList() {
	    assertThat(mapper.getCourseTypeList().size()).isEqualTo(2);
	}
	
	@Test
	@Transactional
	public void getCourseNumberList() {
	    List<Integer> list = mapper.getCourseNumberList("가산", false);
	    assertThat(list.size()).isEqualTo(9);
	}

	@Test
	@Transactional
	public void AddCourse() {
		CourseDTO course = 
				CourseDTO.builder()
				.managerId("bd8c73e1-39c9-11ef-aad4-06a5a7b26ae5")
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
				.managerId("bd8c73e1-39c9-11ef-aad4-06a5a7b26ae5")
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
				.managerId("bd8c73e1-39c9-11ef-aad4-06a5a7b26ae5")
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
