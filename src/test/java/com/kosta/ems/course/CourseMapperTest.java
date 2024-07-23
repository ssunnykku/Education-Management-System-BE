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
	    list = mapper.searchCourseList(0, "강원", 0, 20, false);
	    assertThat(list.size()).isEqualTo(0);
	}
	
	@Test
	@Transactional
	public void getSearchCourseListSize() {
	    int count = mapper.getSearchCourseListSize(0, "가산", 0, 20, false);
	    assertThat(count).isGreaterThan(0);
	    count = mapper.getSearchCourseListSize(0, "강원", 0, 20, false);
        assertThat(count).isEqualTo(0);
	}
	
	@Test
	@Transactional
	public void getCourseTypeList() {
	    assertThat(mapper.getCourseTypeList().size()).isGreaterThan(0);
	}
	
	@Test
	@Transactional
	public void getCourseNumberList() {
	    List<Integer> list = mapper.getCourseNumberList("가산", false);
	    assertThat(list.size()).isGreaterThan(1);
	    list = mapper.getCourseNumberList("강원", false);
        assertThat(list.size()).isEqualTo(0);
	}

	@Test
	@Transactional
	public void AddCourse() {
		CourseDTO course = 
				CourseDTO.builder()
				.managerId("3ddf8303-3eaf-11ef-bd30-0206f94be675")
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
				.managerId("3ddf8303-3eaf-11ef-bd30-0206f94be675")
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
				.managerId("3ddf8303-3eaf-11ef-bd30-0206f94be675")
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
	
	@Test
	@Transactional
	public void getCourseByEndYear() {
	    List<Integer> course= mapper.getCourseNumberListByYear(2024);
	    assertThat(course.size()).isGreaterThan(0);
	}
	@Test
	@Transactional
	public void getCourseNumberYearList() {
		List<Integer> course =mapper.getCourseNumberYearList();
		assertThat(course.size()).isGreaterThan(0);
	}
	@Test
	@Transactional
	public void getStudentsNumberBySeq() {
		assertThat(mapper.getStudentsNumberBySeq(5)).isGreaterThan(0);
	}
	@Test
	@Transactional
	public void getSeqByCourseNumber() {
		assertThat(mapper.getSeqByCourseNumber(277)).isEqualTo(5);
	}
	@Test
	@Transactional
	void getCourseNameByCourseNumber() {
		log.info(mapper.getCourseNameByCourseNumber(277).toString());
	}
}

