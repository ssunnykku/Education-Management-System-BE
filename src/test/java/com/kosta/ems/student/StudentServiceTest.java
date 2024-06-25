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
	// -- 검색 결과 개수 가져오기 (for 페이지네이션)
	// @Test
	void getStudentsByNameOrCourseNumberAmount() {
		log.info(Integer.toString(studentServiceImpl.getStudentsByNameOrCourseNumberAmount("진", "277")));
	}
	
	// -- 검색 결과 데이터 목록 가져오기
	// @Test
	void getStudentsByNameOrCourseNumber() {
		// log.info(studentServiceImpl.getStudentsByNameOrCourseNumber("진", 5).toString());
		log.info(studentServiceImpl.getStudentsByNameOrCourseNumber("진", 277, 1, 2).toString());
	}

	// 수강생 정보 - 수강생 등록
	// -- 입력 id가 등록된 hrdNetId인지 확인
	// @Test
	void findByHrdNetId() {
		log.info(String.valueOf(studentServiceImpl.findByHrdNetId("youyou33wea")));
	}

	// -- 등록된 id인 경우, 기존 수강생 데이터 가져오기
	// @Test
	void getRegisteredStudentBasicInfo() {
		log.info(studentServiceImpl.getRegisteredStudentBasicInfo("youyou33").toString());
	}
	
	// -- students 테이블에 수강생 데이터 등록
	// @Test
	void addStudentBasicInfo() {
		studentServiceImpl.addStudentBasicInfo("test0001", "테스터양씨", "1998-06-30", "서울시 용산구 이촌동 동부이촌2길, 대림아파트 101동 901호", "우리", "10029387655086", "01028768976", "test0001@naver.com", "M", "d893c3ad-2f8f-11ef-b0b2-0206f94be675");
	}

	// -- students_courses 테이블에 수강생 데이터 등록
	// @Test
	void addStudentCourseSeqInfo() {
		studentServiceImpl.addStudentCourseSeqInfo("youyou33", "283");
	}
	
	// 수강생 정보 수정
	// @Test
	void updateSelectedStudentInfo() {
		studentServiceImpl.updateSelectedStudentInfo("박기영","부산광역시 사상구", "부산", "110583195038", "01059341921", "syc1234@gmail.com", "78b21862-32bf-11ef-b0b2-0206f94be675");
	}
	
	// 수강생 삭제
	// @Test
	void deleteSelectedStudent() {
		studentServiceImpl.deleteSelectedStudent("8b48e083-2fa8-11ef-b0b2-0206f94be675");
	}
}