package com.kosta.ems.benefit;

import com.kosta.ems.benefit.dto.*;
import com.kosta.ems.benefit.enums.BenefitCategory;
import com.kosta.ems.student.StudentMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Slf4j
class BenefitMapperTest {
    @Autowired
    private BenefitMapper benefitMapper;
    @Autowired
    private StudentMapper studentMapper;
    @Autowired
    private BenefitServiceImpl benefitServiceImpl;

    BenefitCategory training = BenefitCategory.BENEFIT_CATEGORY_SEQ_TRAINING;
    BenefitCategory meal = BenefitCategory.BENEFIT_CATEGORY_SEQ_MEAL;
    BenefitCategory settlement = BenefitCategory.BENEFIT_CATEGORY_SEQ_SETTLEMENT;

    @Test
    public void selectBenefitTargetTest() {
        log.info("몇개? {} ", benefitMapper.selectBenefitTarget("가산", LocalDate.parse("2024-03-03"), LocalDate.parse("2024-03-30"), "277", "", 20, 0).size());
    }

    //@Test
    @Transactional
    public void insertBenefitSettlementDurationTest() {
        SettlementDurationDTO build = SettlementDurationDTO.builder().settlementDurationEndDate(LocalDate.parse("2024-07-30")).settlementDurationStartDate(LocalDate.parse("2024-07-02")).courseSeq(5).managerId("d893bf71-2f8f-11ef-b0b2-0206f94be675").build();
        log.info(build.toString());
        benefitMapper.insertBenefitSettlementDuration(build);
        log.info(String.valueOf(build.getSettlementDurationSeq()));
    }

   // @Test
    @Transactional
    @DisplayName("훈련수당 입력")
    public void insertBenefitSettlementAmountTest() {
        BenefitDTO build = BenefitDTO.builder()
                .amount(200000)
                .studentId("738006e2-3eb0-11ef-bd30-0206f94be675")
                .settlementDurationSeq(16)
                .benefitsCategoriesSeq(training.getBenefitCategory())
                .managerId("3ddf8303-3eaf-11ef-bd30-0206f94be675")
                .build();
        benefitMapper.insertBenefitSettlementAmount(build);
    }

    //@Test
    void selectBenefitSettlementResultPageTest() {
        List<BenefitTargetInfoDTO> dto = (ArrayList<BenefitTargetInfoDTO>) benefitMapper.resultList("가산", "", "", null, 5, 0);
        for (BenefitTargetInfoDTO d : dto) {
            log.info(d.getBenefitSettlementDate().toString());
        }

        assertThat(dto.size()).isEqualTo(5);
    }

    //@Test
    void selectBenefitSettlementResultTest() {
        List<BenefitTargetInfoDTO> dto = (ArrayList<BenefitTargetInfoDTO>) benefitMapper.resultList("가산", "", "227", LocalDate.parse("2024-03-21"), 5, 0);
        log.info("크기 {}", dto.size());
        for (BenefitTargetInfoDTO d : dto) {
            log.info("이거 뭔데 {} ", d);
            assertThat(d.getBenefitSettlementDate()).isEqualTo(LocalDate.parse("2024-03-21"));
        }
    }

    //@Test
    void selectBenefitSettlementResultTest2() {
        List<BenefitTargetInfoDTO> dto = (ArrayList<BenefitTargetInfoDTO>) benefitMapper.resultList("가산", "", "227", LocalDate.parse("2024-03-21"), 5, 0);
        for (BenefitTargetInfoDTO d : dto) {
            assertThat(d.getCourseNumber()).isEqualTo(277);
        }
    }

    //@Test
    void countSettlementTargetTest() {
        assertThat(benefitMapper.countSettlementTarget("가산", LocalDate.parse("2024-03-03"), LocalDate.parse("2024-04-04"), "277", "")).isEqualTo(5);
    }

    @Test
    void countSettlementResultTest() {
        log.info(String.valueOf(benefitMapper.countSettlementResult("가산", "", "", LocalDate.parse("2024-05-21"))));
    }

    //@Test
    void selectLastSettlementDateTest() {

        assertThat(benefitMapper.selectLastSettlementDate("277")).isEqualTo("2024-03-30");
    }

    //    검증 필요
    @Test
    @Transactional
    void settlementAllCourseStudent() {
        Date today = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        log.info("시적 전 크기는? {} ", benefitMapper.resultList("가산", "", "277", LocalDate.parse(dateFormat.format(today)), 30, 0).size());

        // 정산 정보 받아서 정산하기
        SettlementDurationDTO dto = SettlementDurationDTO.builder()
                .settlementDurationStartDate(LocalDate.parse("2024-04-01"))
                .settlementDurationEndDate(LocalDate.parse("2024-04-30"))
                .courseSeq(5)
                .managerId("3ddf8681-3eaf-11ef-bd30-0206f94be675").build();

        benefitMapper.insertBenefitSettlementDuration(dto);

        log.info("getSettlementDurationSeq {} ", dto.getSettlementDurationSeq());

        // 지원금 정산 대상 가져오기
        List<BenefitTargetInfoDTO> targetList = (ArrayList<BenefitTargetInfoDTO>) benefitMapper.selectBenefitTarget("가산", LocalDate.parse("2024-04-01"), LocalDate.parse("2024-04-30"), "277", "", null, null);

        for (BenefitTargetInfoDTO targetInfo : targetList) {

            int settlementAidAmount = benefitServiceImpl.settlementAid(dto.getSettlementDurationStartDate(), dto.getSettlementDurationEndDate(), targetInfo.getStudentId(), 20);
            int trainingAidAmount = benefitServiceImpl.trainingAid(dto.getSettlementDurationStartDate(), dto.getSettlementDurationEndDate(), targetInfo.getStudentId(), 20);
            int mealAidAmount = benefitServiceImpl.mealAid(dto.getSettlementDurationStartDate(), dto.getSettlementDurationEndDate(), targetInfo.getStudentId(), 20);

            // 훈련수당
            benefitMapper.insertBenefitSettlementAmount(BenefitDTO.builder()
                    .amount(trainingAidAmount)
                    .studentId(targetInfo.getStudentId())
                    .settlementDurationSeq(dto.getSettlementDurationSeq())
                    .managerId(dto.getManagerId())
                    .benefitsCategoriesSeq(training.getBenefitCategory())
                    .build());

            // 정착지원금
            benefitMapper.insertBenefitSettlementAmount(BenefitDTO.builder()
                    .amount(settlementAidAmount)
                    .studentId(targetInfo.getStudentId())
                    .settlementDurationSeq(dto.getSettlementDurationSeq())
                    .managerId(dto.getManagerId())
                    .benefitsCategoriesSeq(settlement.getBenefitCategory())
                    .build());

            // 식비
            benefitMapper.insertBenefitSettlementAmount(BenefitDTO.builder()
                    .amount(settlementAidAmount)
                    .studentId(targetInfo.getStudentId())
                    .settlementDurationSeq(dto.getSettlementDurationSeq())
                    .managerId(dto.getManagerId())
                    .benefitsCategoriesSeq(meal.getBenefitCategory())
                    .build());

        }
        List<BenefitTargetInfoDTO> data = (ArrayList<BenefitTargetInfoDTO>) benefitMapper.resultList("가산", "", "277", LocalDate.parse(dateFormat.format(today)), 30, 0);
        for (BenefitTargetInfoDTO d : data) {
            log.info("결과 출력 {} ", d);
        }

        // 정산 결과 내용과 과정의 학생 수 맞는지 비교하기
    }

    //@Test
    void targetWithoutPagenation() {
        log.info("결과 {} ", benefitMapper.selectBenefitTarget("가산", LocalDate.parse("2024-05-03"), LocalDate.parse("2024-06-02"), "277", "", null, null));
        assertThat(benefitMapper.selectBenefitTarget("가산", LocalDate.parse("2024-05-03"), LocalDate.parse("2024-06-02"), "277", "", null, null).size()).isEqualTo(8);

    }

    @Test
    void BenefitResultTest() {
        List<BenefitTargetInfoDTO> dto = (ArrayList<BenefitTargetInfoDTO>) benefitMapper.resultList("가산", "", "277", null, 30, 0);
        log.info("result {} ", dto);
        for (BenefitTargetInfoDTO d : dto) {
            assertThat(d.getCourseNumber()).isEqualTo("277");
        }
    }

    //@Test
    void getBefitAmountTest() {

        for (BenefitTargetInfoDTO data : benefitMapper.getBefitAmount(LocalDate.parse("2024-07-11"))) {
            assertThat(data.getAmount()).isEqualTo(200000);
        }
    }

}