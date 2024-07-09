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
	    assertThat(result.get(0).getStudentId()).isEqualTo("01236aec-39ca-11ef-aad4-06a5a7b26ae5");
	    
	}

}
