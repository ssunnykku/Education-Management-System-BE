package com.kosta.ems.course;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.kosta.ems.course.CourseDTO;
import com.kosta.ems.course.CourseService;

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
        assertThat(service.searchCourseList(277, "가산", 1, 10, false).size()).isEqualTo(1);
        assertThat(service.searchCourseList(0, "가산", 1, 10, false).size()).isGreaterThan(2);
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

    @Test
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
        course = service.searchCourseList(3000, "가산", 1, 10, false).get(0);
        System.out.println(course);
        course.setCourseName("새로 수정된 이름");
        assertThat(service.editCourse(course)).isTrue();
    }

    @Test
    @Transactional
    public void deleteCourse() {
        assertThat(service.deleteCourse(27, "가산")).isTrue();
    }


    @Test
    void getCurrentCourseList() {
        assertThat(service.getCurrentCourseList(LocalDate.parse("2024-07-14"), "가산")).isNotNull();
    }
}
