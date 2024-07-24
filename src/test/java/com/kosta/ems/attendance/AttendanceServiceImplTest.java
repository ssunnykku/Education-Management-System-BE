package com.kosta.ems.attendance;

import com.kosta.ems.api.ApiService;
import com.kosta.ems.attendance.dto.AttendanceTimeDTO;
import com.kosta.ems.attendance.dto.RequestAcknowledgeDTO;
import com.kosta.ems.attendance.dto.RequestStudentAttendanceDTO;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@SpringBootTest
@Slf4j
class AttendanceServiceImplTest {

    @Autowired
    AttendanceService attendanceService;
    @Autowired
    ApiService apiService;

    // ** 출결 조회 - 수강생 출결 데이터 목록
    @Test
    @DisplayName("출결 조회 - 수강생 출결 조회 cnt")
    void getAttendanceIntegratedListAmount() {
        log.info("가산 지점 전체 출결 조회 cnt: " + Integer.toString(attendanceService.getAttendanceIntegratedListAmount("", 0, "가산")));
        log.info("가산 지점 특정 수강생 출결 조회 cnt: " + Integer.toString(attendanceService.getAttendanceIntegratedListAmount("희", 0, "가산")));
        log.info("가산 지점 특정 기수 출결 cnt: " + Integer.toString(attendanceService.getAttendanceIntegratedListAmount("", 277, "가산")));

        log.info("데이터 없는 경우: " + Integer.toString(attendanceService.getAttendanceIntegratedListAmount("이름없음", -111, "갤럭시")));
    }

    @Test
    @DisplayName("출결 조회 - 수강생 출결 조회 목록")
    void getAttendanceIntegratedList() {
        log.info("가산 지점 전체 출결 조회: " + attendanceService.getAttendanceIntegratedList("", 0, "가산", 1, 10).toString());
        log.info("가산 지점 특정 수강생 출결 조회: " + attendanceService.getAttendanceIntegratedList("임솔", 0, "가산", 1, 10).toString());
        log.info("가산 지점 특정 기수 출결 조회: " + attendanceService.getAttendanceIntegratedList("", 277, "가산", 1, 10).toString());

        log.info("데이터 없는 경우: " + attendanceService.getAttendanceIntegratedList("이름없음", -111, "갤럭시", 1, 10).toString());
    }
    // ** 출결 조회 - 수강생 출결 데이터 목록 (end)


    // ** 출결 입력 - 날짜별 입력된 수강생 출결 상태 목록 조회
    @Test
    @DisplayName("출결 입력 - 날짜별 입력된 수강생 출결 상태 목록 cnt")
    void getAttendanceStatusListAmount() {
        log.info("날짜+가산 지점 전체 출결 cnt: " + Integer.toString(attendanceService.getAttendanceStatusListAmount("2024-07-11", "가산", "", 0)));
        log.info("날짜+가산 지점 특정 수강생 출결 cnt: " + Integer.toString(attendanceService.getAttendanceStatusListAmount("2024-07-11", "가산", "희", 0)));
        log.info("날짜+가산 지점 특정 기수 출결 cnt: " + Integer.toString(attendanceService.getAttendanceStatusListAmount("2024-07-11", "가산", "", 284)));

        log.info("데이터 없는 경우: " + Integer.toString(attendanceService.getAttendanceStatusListAmount("2024-07-11", "가산", "", 0)));
    }

    @Test
    @DisplayName("출결 입력 - 날짜별 입력된 수강생 출결 상태 목록")
    void getAttendanceStatusList() {
        log.info("날짜+가산 지점 전체 출결 상태 목록: " + attendanceService.getAttendanceStatusList("2024-07-15", "가산", "", 0, 1, 10).toString());
        log.info("날짜+가산 지점 특정 수강생 출결 상태 목록: " + attendanceService.getAttendanceStatusList("2024-07-15", "가산", "희", 0, 1, 10).toString());
        log.info("날짜+가산 지점 특정 기수 출결 상태 목록: " + attendanceService.getAttendanceStatusList("2024-07-15", "가산", "", 284, 1, 10).toString());

        log.info("데이터 없는 경우: " + attendanceService.getAttendanceStatusList("2024-07-11", "갤럭시", "이름없음", -111, 1, 10).toString());
    }
    // ** 출결 입력 - 날짜별 입력된 수강생 출결 상태 목록 조회 (end)


    // ** 출결 수정 - 선택한 수강생의 출석 상태 수정
    @Test
    @DisplayName("출결 수정 - 선택한 수강생의 출석 상태 수정")
    @Transactional
    void updateStudentAttendance() {
        List<RequestStudentAttendanceDTO> list = new ArrayList<>();
        RequestStudentAttendanceDTO dto = RequestStudentAttendanceDTO.builder().attendanceDate("2024-07-01").studentId("30d25263-41d5-11ef-bd30-0206f94be675").name("류선재").attendanceStatus("외출").courseNumber(284).academyLocation("가산").studentCourseSeq(30).build();
        RequestStudentAttendanceDTO dto2 = RequestStudentAttendanceDTO.builder().attendanceDate("2024-07-01").studentId("0022bb27-41d9-11ef-bd30-0206f94be675").name("임솔").attendanceStatus("출석").courseNumber(284).academyLocation("가산").studentCourseSeq(31).build();
        list.add(dto);
        list.add(dto2);
        attendanceService.updateStudentAttendance(list);
    }

    // --[출석 인정]
    // --출석 인정 항목 리스트 가져오기
    @Test
    @DisplayName("출결 수정 - 출석인정 항목 리스트 가져오기")
    void getAcknowledgeCategoryList() {
        log.info("활성화된 출석인정 항목 리스트: " + attendanceService.getAcknowledgeCategoryList(1).toString());
        log.info("비활성화된 출석인정 항목 리스트: " + attendanceService.getAcknowledgeCategoryList(0).toString());
    }

    // --출석 인정항목*인정일수 적용하여 출결 상태 반영 (update + insert)
    @Test
    @DisplayName("출결 수정 - 출석인정 항목*인정일수 적용하여 출결 상태 반영_update + insert")
    @Transactional
    void reflectAcknowledgeAttendanceStatus() {
        RequestAcknowledgeDTO dto = RequestAcknowledgeDTO.builder().attendanceStatus("출석 인정").startDate("2024-07-16").endDate("2024-07-17").acknowledgeSeq(4).studentCourseSeq(21).evidentialDocuments("면접참여확인서_양유진_240716.pdf").build();
        attendanceService.reflectAcknowledgeAttendanceStatus(dto);
    }
    // ** 출결 수정 - 선택한 수강생의 출석 상태 수정 (end)

    // ** 출결 입력
    // 1. 특정일의 출결 상태가 등록되지 않은 수강생 목록 가져오기
    @Test
    @DisplayName("출결 등록 - 아직 출결 등록되지 않은 수강생 목록 불러오기")
    void getNoAttendanceStatusStudentList() {
        log.info("출결 등록 다 해서 목록 0: " + attendanceService.getNoAttendanceStatusStudentList("2024-07-15", "가산").toString());
        log.info(attendanceService.getNoAttendanceStatusStudentList("2024-07-15", "강남").toString());
        log.info("출결 등록 다 하지는 않은 목록: " + attendanceService.getNoAttendanceStatusStudentList("2024-07-11", "가산").toString());

        log.info("그냥 데이터 없는 경우: " + attendanceService.getNoAttendanceStatusStudentList("2024-07-11", "갤럭시").toString());
    }

    // 2. 목록의 학생 중 선택한 학생의 출결 상태 등록하기
    @Test
    @DisplayName("출결 등록 - 선택 학생의 출결 상태 등록")
    @Transactional
    void setAttendanceStatus() {
        attendanceService.setAttendanceStatus("지각", "2024-06-19", 34, "3ddf8577-3eaf-11ef-bd30-0206f94be675");
        attendanceService.setAttendanceStatus("출석", "2024-06-19", 31, "3ddf8577-3eaf-11ef-bd30-0206f94be675");
        attendanceService.setAttendanceStatus("외출", "2024-06-19", 30, "3ddf8577-3eaf-11ef-bd30-0206f94be675");
    }

    @Test
    void getTimeByAttendanceDate() {
        log.info("{}", attendanceService.getTimeByAttendanceDate(LocalDate.parse("2024-07-19"), 277));
    }
    
//  모바일 앱 api
    //@Test
    @Transactional
    @DisplayName("모바일 API - 입실/퇴실 시간 CRUD 통합테스트")
    void attendanceTimeCRUDTest() {
        //주의: DB데이터 독립적이지 않다.
        //DB에 오늘의 입실/퇴실시간 데이터가 들어있지 않다고 가정.
        assertThat(apiService.addInTime("f0066b40-44b0-11ef-bd30-0206f94be675")).isTrue();
    }
}