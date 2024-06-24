package com.kosta.ems.student;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@SpringBootTest
@Transactional
class StudentMapperTest {
    @Autowired
    StudentMapper studentMapper;

    // @Test
    public void selectStudentByName() {
        log.info(studentMapper.selectStudentByName("김선").toString());
    }
    
    @Test
    public void findByStudentNameOrCourseNumber() {
    	// log.info(studentMapper.findByStudentNameOrCourseNumber("진", 5).toString());
    	log.info(studentMapper.findByStudentNameOrCourseNumber("진", 277, 0, 10).toString());
    }
}