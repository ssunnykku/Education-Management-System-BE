package com.kosta.ems.student;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.kosta.ems.attendance.UpdateStudentAttendanceStatusDTO;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@SpringBootTest
@Transactional
class StudentMapperTest {
    @Autowired
    StudentMapper studentMapper;

    //@Test
    public void selectStudentByName() {
        log.info(studentMapper.selectStudentByName("김").toString());
    }

    // @Test
    public void findByStudentNumberOrCourseNumberAll() {
        log.info(Integer.toString(studentMapper.findByStudentNumberOrCourseNumberAll("진", 277)));
        ;
    }

    // @Test
    public void findByStudentNameOrCourseNumberList() {
        log.info(Integer.toString(studentMapper.findByStudentNameOrCourseNumberList("", 0, 0, 10).size()));
        log.info(studentMapper.findByStudentNameOrCourseNumberList("", -1, 0, 10).toString());
    }

    // @Test
    public void findByStudentNameOrCourseNumber() {
        log.info(studentMapper.findByStudentNameOrCourseNumberList("진", 277, 0, 10).toString());
    }

    // *0710_수강생 정보 조회
    @Test
    public void selectStudentInfoListCnt() {
        log.info(Integer.toString(studentMapper.selectStudentInfoListCnt(1, "", 0)));
    }

    @Test
    public void selectStudentInfoList() {
        log.info(studentMapper.selectStudentInfoList(1, "", 0, 0, 10).toString());
    }
    // *0710_수강생 정보 조회 (end)


    // 수강생 등록
    // @Test
    public void findByHrdNetId() {
        log.info(Integer.toString(studentMapper.findByHrdNetId("ㅁㅁㅇㄹsyc1234")));
    }

    // @Test
    public void getRegisteredStudentBasicInfo() {
        log.info(studentMapper.selectRegisteredStudentBasicInfo("youyou33").toString());
    }

    // @Test
    public void selectOnGoingCourseList() {
        log.info(studentMapper.selectOnGoingCourseList("가산").toString());
    }

    // @Test
    public void addStudentBasicInfo() {
        studentMapper.addStudentBasicInfo(new AddStudentBasicInfoDTO("test0001", "테스터양씨", LocalDate.of(1995, 02, 17), "서울시 용산구 이촌동 동부이촌2길, 대림아파트 101동 901호", "우리", "10029387655086", "01028768976", "test0001@naver.com", 'M', "d893c3ad-2f8f-11ef-b0b2-0206f94be675", 277));
    }

    // @Test
    void addStudentCourseSeqInfo() {
        AddStudentBasicInfoDTO dto = AddStudentBasicInfoDTO.builder().hrdNetId("qwer1234").courseNumber(289).build();
        studentMapper.addStudentCourseSeqInfo(dto);
    }

    // 수강생 정보 수정
    // @Test
    void updateSelectedStudentInfo() {
        UpdateSelectedStudentInfoDTO dto = UpdateSelectedStudentInfoDTO.builder().studentId("78b21862-32bf-11ef-b0b2-0206f94be675").name("박기영").address("부산광역시 해운대구 해운대로2").bank("부산").account("110583195038").phoneNumber("01059341921").email("syc1234@gmail.com").build();
        studentMapper.updateSelectedStudentInfo(dto);
    }

    // 수강생 삭제
    // @Test
    void deleteSelectedStudent() {
        studentMapper.deleteSelectedStudent("8b48e083-2fa8-11ef-b0b2-0206f94be675");
    }


    // scq로 수강생 기본 정보 가져오기
    // @Test
    void selectStudentInfoByScq() {
        log.info(studentMapper.selectStudentInfoByScq(66).toString());
    }
    
}