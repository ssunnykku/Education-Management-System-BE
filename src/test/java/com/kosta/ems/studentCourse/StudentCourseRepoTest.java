package com.kosta.ems.studentCourse;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.kosta.ems.student.dto.StudentBasicInfoDTO;
import com.kosta.ems.student.StudentMapper;

import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@Slf4j
public class StudentCourseRepoTest {
    @Autowired
    StudentCourseRepo repo;
    @Autowired
    StudentMapper studentMapper;

    @Test
    @Transactional
    public void studentListfindByCourseSeq() {
        List<StudentCourseDTO> result = repo.findByCourseSeq(5);
        assertThat(result.get(0).getStudentId()).isEqualTo("738003dc-3eb0-11ef-bd30-0206f94be675");

    }

    @Test
    @Transactional
    public void countOfStudentsByCourseSeq() {
        assertThat(repo.countByCourseSeq(5)).isGreaterThan(1);
        assertThat(repo.countByCourseSeq(12345678)).isEqualTo(0);
    }

    @Test
    @Transactional
    public void findByStudentId() {
        StudentBasicInfoDTO student = studentMapper.findByStudentNameOrCourseNumberList("손유철", 0, 1, 10).get(0);
        assertThat(repo.findByStudentId(student.getStudentId()).size()).isGreaterThan(0);
        assertThat(repo.findByStudentId("asd").size()).isEqualTo(0);

    }

}
