package com.kosta.ems.attendance;

import com.kosta.ems.attendance.dto.AttendanceTimeDTO;
import com.kosta.ems.attendance.dto.UpdateStudentAttendanceStatusDTO;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

@Slf4j
@SpringBootTest
class AttendanceMapperTest {

    @Autowired
    private AttendanceMapper attendanceMapper;
    @Autowired
    private AttendanceTimeRepo attendanceTimeRepo;

    @Test
    @DisplayName("출결 조회 - 검색결과 cnt 테스트")
    public void selectAttendanceIntegratedListAmount() {
        log.info(Integer.toString(attendanceMapper.selectAttendanceIntegratedListAmount("", 0, "가산").size()));
    }

    @Test
    @DisplayName("출결 조회 - 검색결과 목록 테스트")
    public void selectAttendanceIntegeratedList() {
        log.info(attendanceMapper.selectAttendanceIntegratedList("", 0, "가산", 0, 10).toString());
        log.info(attendanceMapper.selectAttendanceIntegratedList("", 0, "가산", 10, 10).toString());
    }

    @Test
    @DisplayName("출결 입력 - 날짜별 입력된 출결 검색결과 cnt 테스트")
    public void selectAttendanceStatusListAmount() {
        log.info(Integer.toString(attendanceMapper.selectAttendanceStatusListAmount(LocalDate.of(2024, 7, 15), "가산", "", 0)));
    }

    @Test
    @DisplayName("출결 입력 - 날짜별 입력된 출결 검색결과 목록 테스트")
    public void selectAttendanceStatusList() {
        log.info("날짜+특정 강의장의 전체 출결 상태: " + attendanceMapper.selectAttendanceStatusList(LocalDate.of(2024, 7, 15), "가산", "", 0, 0, 10).toString());
        log.info("날짜+특정 학생의 출결 상태: " + attendanceMapper.selectAttendanceStatusList(LocalDate.of(2024, 7, 15), "가산", "선재", 0, 0, 10).toString());
    }


    // [출결 수정]
    // 선택한 수강생의 출석 상태 수정
    @Test
    @DisplayName("출결 수정 - 선택한 수강생의 출석 상태 수정")
    @Transactional
    void updateStudentAttendance() {
        UpdateStudentAttendanceStatusDTO dto = UpdateStudentAttendanceStatusDTO.builder().attendanceStatus("조퇴").attendanceDate(LocalDate.of(2024, 6, 20)).studentCourseSeq(34).build();
        attendanceMapper.updateStudentAttendance(dto);
    }

    // --[출석 인정]
    // --출석 인정 항목 리스트 가져오기
    @Test
    @DisplayName("출결 수정 - 출석인정 항목 리스트 가져오기")
    void selectAcknowledgeCategoryList() {
        log.info("활성화된 출석인정 항목 리스트: " + attendanceMapper.selectAcknowledgeCategoryList(1).toString());
        log.info("비활성화된 출석인정 항목 리스트: " + attendanceMapper.selectAcknowledgeCategoryList(0).toString());
    }

    @Test
    @DisplayName("출결 수정 - 출석인정 항목*인정일수 적용하여 출결 상태 반영_update")
    @Transactional
    void updateAttendanceAcknowledgeStatus() {
        attendanceMapper.updateAttendanceAcknowledgeStatus("출석 인정", "dalifu@hafdf.pdf", 4, LocalDate.of(2024, 7, 15), 21);
    }

    @Test
    @DisplayName("출결 수정 - 출석인정 항목*인정일수 적용하여 출결 상태 반영_insert")
    @Transactional
    void insertAttendanceAcknowledgeStatus() {
        attendanceMapper.insertAttendanceAcknowledgeStatus(LocalDate.of(2024, 7, 17), 21, "출석 인정", "3ddf8577-3eaf-11ef-bd30-0206f94be675", "dalifu@hafdf.pdf", 4);
    }

    // [출결 등록]
    // 1. 특정일의 출결 상태가 등록되지 않은 수강생 목록 가져오기
    @Test  // 확인 완료
    @DisplayName("출결 등록 - 특정일의 출결 상태가 등록되지 않은 수강생 목록 가져오기")
    void selectNoAttendanceStatusStudentList() {
        log.info(attendanceMapper.selectNoAttendanceStatusStudentList(LocalDate.of(2024, 6, 21), "가산").toString());
        log.info(Integer.toString(attendanceMapper.selectNoAttendanceStatusStudentList(LocalDate.of(2024, 6, 21), "가산").size()));
    }

    // 2. 목록의 학생 중 선택한 학생의 출결 상태 등록하기
    @Test  // 확인 완료  *0715
    @DisplayName("출결 등록 - 선택 학생의 출결 상태 등록")
    @Transactional
    void insertAttendanceStatus() {
        UpdateStudentAttendanceStatusDTO dto = UpdateStudentAttendanceStatusDTO.builder().attendanceStatus("출석").attendanceDate(LocalDate.of(2024, 6, 20)).studentCourseSeq(34).managerId("3ddf8577-3eaf-11ef-bd30-0206f94be675").build();
        attendanceMapper.insertAttendanceStatus(dto);
    }

}



