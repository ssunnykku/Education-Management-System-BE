package com.kosta.ems.student;

import com.kosta.ems.student.dto.RegisteredStudentInfoDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;

import static org.junit.jupiter.api.Assertions.assertNull;

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
    @Test
    void getStudentsByNameOrCourseNumber() {
        log.info(studentService.getStudentsByNameOrCourseNumberList("", 0, 1, 10).toString());
    }

    // *0710_수강생 정보 조회
    @Test
    @DisplayName("수강생 목록 조회 Cnt 테스트")
    void getStudentInfoListCnt() {
        log.info("활성화된 '가산'지점의 전체 수강생 조회 cnt: " + Integer.toString(studentService.getStudentInfoListCnt(1, "", 0, "가산")));
        log.info("비활성화된 '가산'지점의 전체 수강생 조회 cnt: " + Integer.toString(studentService.getStudentInfoListCnt(0, "", 0, "가산")));
        log.info("활성화된 '가산'지점의 특정 기수 수강생 조회 cnt: " + Integer.toString(studentService.getStudentInfoListCnt(1, "", 284, "가산")));
        log.info("비활성화된 '가산'지점의 특정 기수 수강생 조회 cnt: " + Integer.toString(studentService.getStudentInfoListCnt(0, "", 284, "가산")));
        log.info("활성화된 '가산'지점의 특정 수강생 조회 cnt: " + Integer.toString(studentService.getStudentInfoListCnt(1, "선재", 0, "가산")));
        log.info("비활성화된 '가산'지점의 특정 수강생 조회 cnt: " + Integer.toString(studentService.getStudentInfoListCnt(0, "선재", 0, "가산")));

        log.info("DB에 없는 데이터 조회 cnt 테스트 코드 모음");
        log.info(Integer.toString(studentService.getStudentInfoListCnt(1, "미니언", 0, "가산")));
        log.info(Integer.toString(studentService.getStudentInfoListCnt(0, "미니언", 0, "가산")));
        log.info(Integer.toString(studentService.getStudentInfoListCnt(1, "", -100, "가산")));
        log.info(Integer.toString(studentService.getStudentInfoListCnt(0, "", -100, "가산")));
        log.info(Integer.toString(studentService.getStudentInfoListCnt(1, "임솔", 0, "갤럭시")));
        log.info(Integer.toString(studentService.getStudentInfoListCnt(0, "임솔", 0, "갤럭시")));
        log.info(Integer.toString(studentService.getStudentInfoListCnt(1, "", 277, "갤럭시")));
        log.info(Integer.toString(studentService.getStudentInfoListCnt(0, "", 277, "갤럭시")));
    }

    @Test
    @DisplayName("수강생 목록 조회 테스트")
    void getStudentInfoList() {
        log.info("활성화된 '가산'지점의 전체 수강생 조회: " + studentService.getStudentInfoList(1, "", 0, "가산", 1, 10).toString());
        log.info("비활성화된 '가산'지점의 전체 수강생 조회: " + studentService.getStudentInfoList(0, "", 0, "가산", 1, 10).toString());
        log.info("활성화된 '가산'지점의 특정 기수 수강생 조회: " + studentService.getStudentInfoList(1, "", 284, "가산", 1, 10).toString());
        log.info("비활성화된 '가산'지점의 특정 기수 수강생 조회: " + studentService.getStudentInfoList(0, "", 284, "가산", 1, 10).toString());
        log.info("활성화된 '가산'지점의 특정 수강생 조회: " + studentService.getStudentInfoList(1, "선재", 0, "가산", 1, 10).toString());
        log.info("비활성화된 '가산'지점의 특정 수강생 조회: " + studentService.getStudentInfoList(0, "선재", 0, "가산", 1, 10).toString());

        log.info("DB에 없는 데이터 조회 테스트 코드 모음");
        log.info(studentService.getStudentInfoList(1, "미니언", 0, "가산", 1, 10).toString());
        log.info(studentService.getStudentInfoList(0, "미니언", 0, "가산", 1, 10).toString());
        log.info(studentService.getStudentInfoList(1, "", -100, "가산", 1, 10).toString());
        log.info(studentService.getStudentInfoList(0, "", -100, "가산", 1, 10).toString());
        log.info(studentService.getStudentInfoList(1, "임솔", 0, "갤럭시", 1, 10).toString());
        log.info(studentService.getStudentInfoList(0, "임솔", 0, "갤럭시", 1, 10).toString());
        log.info(studentService.getStudentInfoList(1, "", 277, "갤럭시", 1, 10).toString());
        log.info(studentService.getStudentInfoList(0, "", 277, "갤럭시", 1, 10).toString());
    }
    // *0710_수강생 정보 조회 (end)

    // *0710_선택한 수강생 id로 수강내역 데이터 가져오기
    @Test
    void getStudentCourseHistory() {
        log.info(studentService.getStudentCourseHistory("01236f03-39ca-11ef-aad4-06a5a7b26ae5").toString());
    }

    // 수강생 정보 - 수강생 등록
    // -- 입력 id가 등록된 hrdNetId인지 확인
    @Test
    void findByHrdNetId() {
        log.info(String.valueOf(studentService.findByHrdNetId("youyou33wea")));
    }

    // -- 등록된 id인 경우, 기존 수강생 데이터 가져오기
    // *0715 테스트 코드 검토 완료
    @Test
    @DisplayName("기존 수강생 데이터 가져오기")
    void getRegisteredStudentBasicInfo() {
        log.info("기존 등록 수강생O: " + studentService.getRegisteredStudentBasicInfo("moving99bong").toString());

        String unregisteredHrdNetId = "없는아이디";
        RegisteredStudentInfoDTO unregisteredStudentInfo = studentService.getRegisteredStudentBasicInfo(unregisteredHrdNetId);
        assertNull(unregisteredStudentInfo, "없는 아이디 학생 정보 확인");
        log.info("기존 등록 수강생X: 정상 실행됨");
    }

    // -- 현재 날짜 기준 진행 중 + 최대 정원까지 수강 접수 안 된 접수 가능한 교육과정 목록 가져오기
    // *0715 테스트 코드 검토 완료
    @Test
    @DisplayName("수강생 등록 - 진행 중+접수 가능 교육과정 목록 조회 테스트")
    void getOnGoingCourseList() {
        log.info("교육장 - '가산': " + studentService.getOnGoingCourseList("가산").toString());
        log.info("교육장 - '강남': " + studentService.getOnGoingCourseList("강남").toString());

        log.info("교육장 - '갤럭시': " + studentService.getOnGoingCourseList("갤럭시").toString());
    }


    // -- students_courses 테이블에 수강생 데이터 등록
    @Test
    void addStudentCourseSeqInfo() {
        studentService.setStudentCourseSeqInfo("yyj1234", "284", "3ddf8577-3eaf-11ef-bd30-0206f94be675");
    }

    // 수강생 정보 수정
    @Test
    void updateSelectedStudentInfo() {
        studentService.updateSelectedStudentInfo("박기영", "부산광역시 사상구 사상중앙로12길", "부산", "110583195038", "01059341921", "syc1234@gmail.com", "78b21862-32bf-11ef-b0b2-0206f94be675", 1);
    }

    // 수강생 삭제
    @Test
    void removeSelectedStudent() {
        studentService.removeSelectedStudent("8b48e083-2fa8-11ef-b0b2-0206f94be675");
    }

    @Test
    void getStudentInfoList2() {
        studentService.getStudentInfoList2(1, "", 0, "가산", 1, 10);
    }
}