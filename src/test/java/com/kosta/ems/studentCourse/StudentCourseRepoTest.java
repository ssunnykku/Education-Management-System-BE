package com.kosta.ems.studentCourse;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;


import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@Slf4j
public class StudentCourseRepoTest {
	@Autowired
	StudentCourseRepo repo;
	
	@Test
	@Transactional
	public void studentListfindByCourseSeq() {
	    List<StudentCourseDTO> result = repo.findByCourseSeq(5);
	    assertThat(result.get(0).getStudentId()).isEqualTo("738003dc-3eb0-11ef-bd30-0206f94be675");
	    
	}
	
	@Test
	@Transactional
	public void countOfStudentsByCourseSeq() {
	    assertThat(repo.countByCourseSeq(5)).isEqualTo(9);
	}

}
