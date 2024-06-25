package com.kosta.ems.student;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Slf4j
@SpringBootTest
@Transactional
class StudentMapperTest {
    @Autowired
    StudentMapper studentMapper;

    // @Test
    public void selectStudentByName() {
        log.info(studentMapper.selectStudentByName("김").toString());
    }
    
    // @Test
    public void findByStudentNumberOrCourseNumberAll() {
    	log.info(Integer.toString(studentMapper.findByStudentNumberOrCourseNumberAll("진", 277)));;
    }
    
    // @Test
    public void findByStudentNameOrCourseNumber() {
    	// log.info(studentMapper.findByStudentNameOrCourseNumber("진", 5).toString());
    	log.info(studentMapper.findByStudentNameOrCourseNumber("진", 277, 0, 10).toString());
    }

    // 수강생 등록
    // @Test
    public void findByHrdNetId() {
        log.info(Integer.toString(studentMapper.findByHrdNetId("youyou33")));
    }

    // @Test
    public void getRegisteredStudentBasicInfo() {
        log.info(studentMapper.getRegisteredStudentBasicInfo("youyou33").toString());
    }
    
    // @Test
    public void addStudentBasicInfo() {
        studentMapper.addStudentBasicInfo(new AddStudentBasicInfoDTO("test0001", "테스터양씨", LocalDate.of(1995,02,17), "서울시 용산구 이촌동 동부이촌2길, 대림아파트 101동 901호", "우리", "10029387655086", "01028768976", "test0001@naver.com", 'M', "d893c3ad-2f8f-11ef-b0b2-0206f94be675"));
    }

    // @Test
    void addStudentCourseSeqInfo() {
        studentMapper.addStudentCourseSeqInfo(new AddStudentCourseSeqDTO("youyou33", 283));
    }
    
    // 수강생 정보 수정
    // @Test
    void updateSelectedStudentInfo() {
    	studentMapper.updateSelectedStudentInfo(new UpdateSelectedStudentInfoDTO("박기영", "부산광역시 해운대구 해운대로", "부산", "110583195038", "01059341921", "syc1234@gmail.com"), "78b21862-32bf-11ef-b0b2-0206f94be675");
    }
    
    // 수강생 삭제
    // @Test
    void deleteSelectedStudent() {
    	studentMapper.deleteSelectedStudent("8b48e083-2fa8-11ef-b0b2-0206f94be675");
    }
}