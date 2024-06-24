package com.kosta.ems.student;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest
@Transactional
public class StudentServiceTest {
	@Autowired
	StudentServiceImpl studentServiceImpl;
	
	// 수강생 정보 - 정보 조회
	@Test
	void getStudentsByNameOrCourseNumber() {
		// log.info(studentServiceImpl.getStudentsByNameOrCourseNumber("진", 5).toString());
		log.info(studentServiceImpl.getStudentsByNameOrCourseNumber("진", 277, 1, 2).toString());
	}
}
