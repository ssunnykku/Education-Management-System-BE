package com.kosta.ems.student;

import java.util.Collection;

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
	StudentService studentService;
	// 수강생 정보 - 정보 조회
	// -- 검색 결과 개수 가져오기 (for 페이지네이션)
	// @Test
	void getStudentsByNameOrCourseNumberAmount() {
		log.info(Integer.toString(studentService.getStudentsByNameOrCourseNumberAmount("진", 277)));
	}
	
	// -- 검색 결과 데이터 목록 가져오기
	// @Test
	void getStudentsByNameOrCourseNumber() {
		log.info(studentService.getStudentsByNameOrCourseNumberList("진", 277, 1, 2).toString());
	}

	// 수강생 정보 - 수강생 등록
	// -- 입력 id가 등록된 hrdNetId인지 확인
	// @Test
	void findByHrdNetId() {
		log.info(String.valueOf(studentService.findByHrdNetId("youyou33wea")));
	}

	// -- 등록된 id인 경우, 기존 수강생 데이터 가져오기
	// @Test
	void getRegisteredStudentBasicInfo() {
		log.info(studentService.getRegisteredStudentBasicInfo("youyou33").toString());
	}

	// -- 현재 날짜 기준 진행 중 + 최대 정원까지 수강 접수 안 된 접수 가능한 교육과정 목록 가져오기
	// @Test
	void getOnGoingCourseList() {
		log.info(studentService.getOnGoingCourseList("가산").toString());
	}
	
	// -- students 테이블에 수강생 데이터 등록
	// @Test
	void addStudentBasicInfo() {
		studentService.setStudentBasicInfo("qwer1234", "김보리", "1996-06-30", "서울시 용산구 이촌동 동부이촌2길, 대림아파트 101동 901호", "우리", "10029387655086", "01028768976", "qwer1234@naver.com", "M", "d893c3ad-2f8f-11ef-b0b2-0206f94be675", "277");
	}

	// -- students_courses 테이블에 수강생 데이터 등록
	// @Test
	void addStudentCourseSeqInfo() {
		studentService.setStudentCourseSeqInfo("yyj1234", "284");
	}
	
	// 수강생 정보 수정
	// @Test
	void updateSelectedStudentInfo() {
		studentService.updateSelectedStudentInfo("박기영","부산광역시 사상구 사상중앙로12길", "부산", "110583195038", "01059341921", "syc1234@gmail.com", "78b21862-32bf-11ef-b0b2-0206f94be675");
	}
	
	// 수강생 삭제
	// @Test
	void removeSelectedStudent() {
		studentService.removeSelectedStudent("8b48e083-2fa8-11ef-b0b2-0206f94be675");
	}
	
}