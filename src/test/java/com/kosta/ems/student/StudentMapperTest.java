package com.kosta.ems.student;

import com.kosta.ems.student.dto.AddStudentBasicInfoDTO;
import com.kosta.ems.student.dto.StudentCourseInfoDTO;
import com.kosta.ems.student.dto.UpdateSelectedStudentInfoDTO;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

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
    @DisplayName("수강생 목록 조회 Cnt 테스트")
    public void selectStudentInfoListCnt() {
        log.info("활성화된 '가산'지점의 전체 수강생 조회 cnt: " + Integer.toString(studentMapper.selectStudentInfoListCnt(1, "", 0, "가산")));
        log.info("비활성화된 '가산'지점의 전체 수강생 조회 cnt: " + Integer.toString(studentMapper.selectStudentInfoListCnt(0, "", 0, "가산")));
        log.info("활성화된 '가산'지점의 특정 기수 수강생 조회 cnt: " + Integer.toString(studentMapper.selectStudentInfoListCnt(1, "", 277, "가산")));
        log.info("비활성화된 '가산'지점의 특정 기수 수강생 조회 cnt: " + Integer.toString(studentMapper.selectStudentInfoListCnt(0, "", 277, "가산")));
        log.info("활성화된 '가산'지점의 특정 수강생 조회 cnt: " + Integer.toString(studentMapper.selectStudentInfoListCnt(1, "유진", 0, "가산")));
        log.info("비활성화된 '가산'지점의 특정 수강생 조회 cnt: " + Integer.toString(studentMapper.selectStudentInfoListCnt(0, "유진", 0, "가산")));

        log.info("DB에 없는 데이터 조회 cnt 테스트 코드 모음");
        log.info(Integer.toString(studentMapper.selectStudentInfoListCnt(1, "미니언", 0, "가산")));
        log.info(Integer.toString(studentMapper.selectStudentInfoListCnt(0, "미니언", 0, "가산")));
        log.info(Integer.toString(studentMapper.selectStudentInfoListCnt(1, "", -100, "가산")));
        log.info(Integer.toString(studentMapper.selectStudentInfoListCnt(0, "", -100, "가산")));
        log.info(Integer.toString(studentMapper.selectStudentInfoListCnt(1, "양유진", 0, "갤럭시")));
        log.info(Integer.toString(studentMapper.selectStudentInfoListCnt(0, "양유진", 0, "갤럭시")));
        log.info(Integer.toString(studentMapper.selectStudentInfoListCnt(1, "", 277, "갤럭시")));
        log.info(Integer.toString(studentMapper.selectStudentInfoListCnt(0, "", 277, "갤럭시")));
    }

    @Test
    @DisplayName("수강생 목록 조회 테스트")
    public void selectStudentInfoList() {
        log.info("활성화된 '가산'지점의 전체 수강생 조회: " + studentMapper.selectStudentInfoList(1, "", 0, "가산", 0, 10).toString());
        log.info("비활성화된 '가산'지점의 전체 수강생 조회: " + studentMapper.selectStudentInfoList(0, "", 0, "가산", 0, 10).toString());
        log.info("활성화된 '가산'지점의 특정 기수 수강생 조회: " + studentMapper.selectStudentInfoList(1, "", 277, "가산", 0, 10).toString());
        log.info("비활성화된 '가산'지점의 특정 기수 수강생 조회: " + studentMapper.selectStudentInfoList(0, "", 277, "가산", 0, 10).toString());
        log.info("활성화된 '가산'지점의 특정 수강생 조회: " + studentMapper.selectStudentInfoList(1, "유진", 0, "가산", 0, 10).toString());
        log.info("비활성화된 '가산'지점의 특정 수강생 조회: " + studentMapper.selectStudentInfoList(0, "유진", 0, "가산", 0, 10).toString());

        log.info("DB에 없는 데이터 조회 테스트 코드 모음");
        log.info(studentMapper.selectStudentInfoList(1, "미니언", 0, "가산", 0, 10).toString());
        log.info(studentMapper.selectStudentInfoList(0, "미니언", 0, "가산", 0, 10).toString());
        log.info(studentMapper.selectStudentInfoList(1, "", -100, "가산", 0, 10).toString());
        log.info(studentMapper.selectStudentInfoList(0, "", -100, "가산", 0, 10).toString());
        log.info(studentMapper.selectStudentInfoList(1, "양유진", 0, "갤럭시", 0, 10).toString());
        log.info(studentMapper.selectStudentInfoList(0, "양유진", 0, "갤럭시", 0, 10).toString());
        log.info(studentMapper.selectStudentInfoList(1, "", 277, "갤럭시", 0, 10).toString());
        log.info(studentMapper.selectStudentInfoList(0, "", 277, "갤럭시", 0, 10).toString());
    }
    // *0710_수강생 정보 조회 // *0715 테스트 코드 검토 완료 (end)


    // 수강생 등록
    @Test
    @DisplayName("수강생 등록 - hrdNetId 등록 여부 테스트")
    public void findByHrdNetId() {
        log.info("등록된 hrdNetId로 테스트: " + Integer.toString(studentMapper.findByHrdNetId("syc1234")));
        log.info("미등록된 hrdNetId로 테스트: " + Integer.toString(studentMapper.findByHrdNetId("ㅁㅁㅇㄹsyc1234")));
    }

    // @Test
    public void getRegisteredStudentBasicInfo() {
        log.info(studentMapper.selectRegisteredStudentBasicInfo("youyou33").toString());
    }


    // *0715 테스트 코드 검토 완료
    @Test
    @DisplayName("수강생 등록 - 진행 중+접수 가능 교육과정 목록 조회 테스트")
    public void selectOnGoingCourseList() {
        log.info("교육장 - '가산': " + studentMapper.selectOnGoingCourseList("가산").toString());
        log.info("교육장 - '강남': " + studentMapper.selectOnGoingCourseList("강남").toString());

        log.info("교육장 - '갤럭시': " + studentMapper.selectOnGoingCourseList("갤럭시").toString());
    }

    // 수강생 교육과정 수강신청(등록)
    @Test
    void addStudentCourseSeqInfo() {
        AddStudentBasicInfoDTO dto = AddStudentBasicInfoDTO.builder().hrdNetId("hellonuri7").courseNumber(289).managerId("3ddf8577-3eaf-11ef-bd30-0206f94be675").build();
        studentMapper.addStudentCourseSeqInfo(dto);
    }

    // 수강생 정보 수정
    @Test
    void updateSelectedStudentInfo() {
        UpdateSelectedStudentInfoDTO dto = UpdateSelectedStudentInfoDTO.builder().studentId("78b21862-32bf-11ef-b0b2-0206f94be675").name("박기영").address("부산광역시 해운대구 해운대로2").bank("부산").account("110583195038").phoneNumber("01059341921").email("syc1234@gmail.com").build();
        studentMapper.updateSelectedStudentInfo(dto);
    }

    // 수강생 삭제
    @Test
    void deleteSelectedStudent() {
        studentMapper.deleteSelectedStudent("8b48e083-2fa8-11ef-b0b2-0206f94be675");
    }


    // scq로 수강생 기본 정보 가져오기
    @Test
    void selectStudentInfoByScq() {
        log.info(studentMapper.selectStudentInfoByScq(66).toString());
    }

    @Test
    void selectStudentListByCourseSeqTest() {

        List<StudentCourseInfoDTO> list = studentMapper.selectStudentListBycourseSeq(19);

        for (int i = 0; i < list.size(); i++) {
            assertThat(list.get(i).getCourseNumber()).isEqualTo(277);
        }

    }

    @Test
    void updateStudnet() {
        String studentId = "738003dc-3eb0-11ef-bd30-0206f94be675";
        String currentPassword = "1234";
        String newPassword = "4321";
        String phoneNumber = "01012345678";
        String bank = "국민";
        String accountNumber = "110123456789";
        String email = "test@test.com";
        assertThat(studentMapper.updateStudentContactInfo(studentId, currentPassword, newPassword, phoneNumber, bank, accountNumber, email)).isTrue();
    }

}