package com.kosta.ems.benefit;

import com.kosta.ems.benefit.dto.BenefitTargetInfoDTO;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.lessThanOrEqualTo;

@SpringBootTest
@Slf4j
class BenefitServiceImplTest {
    @Autowired
    BenefitServiceImpl benefitServiceImpl;
    @Autowired
    BenefitService benefitService;
    @Autowired
    BenefitMapper benefitMapper;

    @Test
    void getBenefitTargetListTest() {
        List<BenefitTargetInfoDTO> dto = (ArrayList<BenefitTargetInfoDTO>) benefitService.getBenefitTargetList(BenefitTargetInfoDTO.builder().academyLocation("가산").settlementDurationStartDate(LocalDate.parse("2025-03-03")).settlementDurationEndDate(LocalDate.parse("2025-03-30")).courseNumber("277").lectureDays(20).name("").build());
        log.info("결과 {} ", dto);
        log.info("크기 {} ", dto.size());
        for (BenefitTargetInfoDTO data : dto) {
            assertThat(data.getName()).contains("");
            log.info(data.toString());
        }
    }

    @Test
    @DisplayName("식비 계산 테스트")
    void getBenefitTargetListMealAidTest() {
        List<BenefitTargetInfoDTO> dto = (ArrayList<BenefitTargetInfoDTO>) benefitService.getBenefitTargetList(BenefitTargetInfoDTO.builder().settlementDurationStartDate(LocalDate.parse("2024-07-31")).settlementDurationEndDate(LocalDate.parse("2024-08-04")).courseNumber("277").lectureDays(20).name("박").build());

        for (BenefitTargetInfoDTO data : dto) {
            assertThat(data.getName()).contains("박");
            assertThat(data.getMealAidAmount()).isEqualTo(benefitServiceImpl.mealAid(LocalDate.parse("2024-03-03"), LocalDate.parse("2024-04-04"), data.getStudentId(), 20));
        }
    }

    @Test
    @DisplayName("훈련 수당 테스트")
    void getBenefitTargetListTrainingAidTest() {
        List<BenefitTargetInfoDTO> dto = (ArrayList<BenefitTargetInfoDTO>) benefitService.getBenefitTargetList(BenefitTargetInfoDTO.builder().settlementDurationStartDate(LocalDate.parse("2024-07-31")).settlementDurationEndDate(LocalDate.parse("2024-08-04")).courseNumber("277").lectureDays(20).build());

        for (BenefitTargetInfoDTO data : dto) {
            assertThat(data.getTrainingAidAmount()).isEqualTo(benefitServiceImpl.trainingAid(LocalDate.parse("2024-03-03"), LocalDate.parse("2024-04-04"), data.getStudentId(), 20));
        }
    }

    // @Test
    void countMealAidTest() {
        int data = benefitServiceImpl.mealAid(LocalDate.parse("2025-04-03"), LocalDate.parse("2025-04-04"), "7380091c-3eb0-11ef-bd30-0206f94be675", 20);
        log.info(String.valueOf(data));
        assertThat(data).isEqualTo(125000);
    }

    @Test
    void trainingAidTest() {
        double data = benefitServiceImpl.trainingAid(LocalDate.parse("2024-03-03"), LocalDate.parse("2024-04-04"), "efa146c5-2fa7-11ef-b0b2-0206f94be675", 20);
        log.info(String.valueOf(data));
    }

    @Test
    void getBenefitSettlementResultTest() {

        ArrayList<BenefitTargetInfoDTO> result = (ArrayList<BenefitTargetInfoDTO>) benefitService.getBenefitSettlementResult(BenefitTargetInfoDTO.builder().name("").courseNumber("").benefitSettlementDate(LocalDate.parse("2024-05-21")).build(), 1, 10);

        log.info(result.toString());
        log.info(String.valueOf(result.size()));

        for (BenefitTargetInfoDTO dto : result) {
            assertThat(dto.getBenefitSettlementDate()).isEqualTo(LocalDate.parse("2024-05-21"));
        }

    }

    @Test
    void getBenefitSettlementResultPageTest() {

        ArrayList<BenefitTargetInfoDTO> result = (ArrayList<BenefitTargetInfoDTO>) benefitService.getBenefitSettlementResult(BenefitTargetInfoDTO.builder().name("").courseNumber("").benefitSettlementDate(LocalDate.parse("2024-05-21")).build(), 1, 10);
        log.info(result.toString());
        org.hamcrest.MatcherAssert.assertThat(result.size(), lessThanOrEqualTo(10));
    }

    @Test
    @DisplayName("비수도권 거주")
    void settlementAidTest() {
        log.info(String.valueOf(benefitServiceImpl.settlementAid(LocalDate.parse("2024-03-03"), LocalDate.parse("2024-04-04"), "a190b6a6-3f2e-11ef-bd30-0206f94be675", 20)));
    }

    @Test
    @DisplayName("80% 수료, 수도권 거주")
    void settlementAidTest2() {
        log.info(String.valueOf(benefitServiceImpl.settlementAid(LocalDate.parse("2024-03-03"), LocalDate.parse("2024-04-04"), "738006e2-3eb0-11ef-bd30-0206f94be675", 20)));
        assertThat(benefitServiceImpl.settlementAid(LocalDate.parse("2024-03-03"), LocalDate.parse("2024-04-04"), "738006e2-3eb0-11ef-bd30-0206f94be675", 20)).isEqualTo(0);
    }

    @Test
    void attendanceDaysTest() {
        double data = benefitServiceImpl.attendanceDays(LocalDate.parse("2024-03-03"), LocalDate.parse("2024-04-04"), "efa146c5-2fa7-11ef-b0b2-0206f94be675") / (double) 20;
    }

    //@Test
    void countBenefitSettlementTest() {
        assertThat(benefitService.countBenefitSettlement(BenefitTargetInfoDTO.builder().academyLocation("가산").settlementDurationStartDate(LocalDate.parse("2024-03-03")).settlementDurationEndDate(LocalDate.parse("2024-04-04")).courseNumber("277").build())).isEqualTo(19);
    }

    //@Test
    @Transactional
    void settlementCourseStudent() {
        BenefitTargetInfoDTO dto = BenefitTargetInfoDTO.builder()
                .academyLocation("가산")
                .settlementDurationStartDate(LocalDate.parse("2024-04-03"))
                .settlementDurationEndDate(LocalDate.parse("2024-05-02"))
                .courseNumber("277")
                .managerId("d893bf71-2f8f-11ef-b0b2-0206f94be675")
                .name("")
                .lectureDays(20)
                .build();

        benefitService.setBenefitSettlement(dto);

        // 정산 결과 내용과 과정의 학생 수 맞는지 비교하기
        assertThat(benefitService.getBenefitSettlementResult(dto, 1, 100).size()).isEqualTo(benefitService.countBenefitResult(dto));

    }


}

