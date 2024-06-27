package com.kosta.ems.benefit;

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

    @Test
    void getBenefitTargetListTest() {
        List<BenefitTargetInfoDTO> dto = (ArrayList<BenefitTargetInfoDTO>) benefitService.getBenefitTargetList(BenefitTargetInfoDTO.builder().startDate(LocalDate.parse("2024-03-03")).endDate(LocalDate.parse("2024-04-04")).courseNumber(277).lectureDays(20).build(), 1, 10);

        for (BenefitTargetInfoDTO data : dto) {
            log.info(data.toString());
        }
    }

    @Test
    @DisplayName("식비 계산 테스트")
    void getBenefitTargetListMealAidTest() {
        List<BenefitTargetInfoDTO> dto = (ArrayList<BenefitTargetInfoDTO>) benefitService.getBenefitTargetList(BenefitTargetInfoDTO.builder().startDate(LocalDate.parse("2024-03-03")).endDate(LocalDate.parse("2024-04-04")).courseNumber(277).lectureDays(20).build(), 1, 10);

        for (BenefitTargetInfoDTO data : dto) {
            assertThat(data.getMealAidAmount()).isEqualTo(benefitServiceImpl.mealAid(LocalDate.parse("2024-03-03"), LocalDate.parse("2024-04-04"), data.getStudentId(), 20));
        }
    }

    @Test
    @DisplayName("훈련 수당 테스트")
    void getBenefitTargetListTrainingAidTest() {
        List<BenefitTargetInfoDTO> dto = (ArrayList<BenefitTargetInfoDTO>) benefitService.getBenefitTargetList(BenefitTargetInfoDTO.builder().startDate(LocalDate.parse("2024-03-03")).endDate(LocalDate.parse("2024-04-04")).courseNumber(277).lectureDays(20).build(), 1, 10);

        for (BenefitTargetInfoDTO data : dto) {
            assertThat(data.getTrainingAidAmount()).isEqualTo(benefitServiceImpl.trainingAid(LocalDate.parse("2024-03-03"), LocalDate.parse("2024-04-04"), data.getStudentId(), 20));
        }
    }

    @Test
    void countMealAidTest() {
        int data = benefitServiceImpl.mealAid(LocalDate.parse("2024-03-03"), LocalDate.parse("2024-04-04"), "efa146c5-2fa7-11ef-b0b2-0206f94be675", 20);
        log.info(String.valueOf(data));
        assertThat(data).isEqualTo(130000);
    }

    @Test
    void trainingAidTest() {
        double data = benefitServiceImpl.trainingAid(LocalDate.parse("2024-03-03"), LocalDate.parse("2024-04-04"), "efa146c5-2fa7-11ef-b0b2-0206f94be675", 20);
        log.info(String.valueOf(data));
    }

    @Test
    @Transactional
    void setBenefitSettlementDTO() {
        BenefitSettlementReqDTO benefitSettlementReqDTO = BenefitSettlementReqDTO.builder()
                .settlementDurationEndDate(LocalDate.parse("2024-07-30"))
                .settlementDurationStartDate(LocalDate.parse("2024-07-02"))
                .courseSeq(19).managerId("d893bf71-2f8f-11ef-b0b2-0206f94be675")
                .trainingAidAmount(200000)
                .mealAidAmount(9500)
                .settlementAidAmount(200000)
                .studentId("efa1441a-2fa7-11ef-b0b2-0206f94be675")
                .settlementDurationSeq(4)
                .build();
        log.info(benefitSettlementReqDTO.toString());
        benefitService.setBenefitSettlement(benefitSettlementReqDTO);

    }

    @Test
    void getBenefitSettlementResultTest() {

        ArrayList<BenefitSettlementResultDTO> result = (ArrayList<BenefitSettlementResultDTO>) benefitService.getBenefitSettlementResult(BenefitSettlementReqDTO.builder().name("").courseNumber("").benefitSettlementDate(LocalDate.parse("2024-05-21")).build(), 1, 10);

        log.info(result.toString());
        log.info(String.valueOf(result.size()));

        for (BenefitSettlementResultDTO dto : result) {
            assertThat(dto.getBenefitSettlementDate()).isEqualTo(LocalDate.parse("2024-05-21"));
        }

    }

    @Test
    void getBenefitSettlementResultPageTest() {

        ArrayList<BenefitSettlementResultDTO> result = (ArrayList<BenefitSettlementResultDTO>) benefitService.getBenefitSettlementResult(BenefitSettlementReqDTO.builder().name("").courseNumber("").benefitSettlementDate(LocalDate.parse("2024-05-21")).build(), 1, 10);
        log.info(result.toString());
        org.hamcrest.MatcherAssert.assertThat(result.size(), lessThanOrEqualTo(10));
    }

    @Test
    @DisplayName("비수도권 거주")
    void settlementAidTest() {
        log.info(String.valueOf(benefitServiceImpl.settlementAid(LocalDate.parse("2024-03-03"), LocalDate.parse("2024-04-04"), "814774e6-3072-11ef-b0b2-0206f94be675", 20)));
    }

    @Test
    @DisplayName("80% 수료, 수도권 거주")
    void settlementAidTest2() {
        log.info(String.valueOf(benefitServiceImpl.settlementAid(LocalDate.parse("2024-03-03"), LocalDate.parse("2024-04-04"), "efa146c5-2fa7-11ef-b0b2-0206f94be675", 20)));
        assertThat(benefitServiceImpl.settlementAid(LocalDate.parse("2024-03-03"), LocalDate.parse("2024-04-04"), "efa146c5-2fa7-11ef-b0b2-0206f94be675", 20)).isEqualTo(0);
    }

    @Test
    void attendanceDaysTest() {
        double data = benefitServiceImpl.attendanceDays(LocalDate.parse("2024-03-03"), LocalDate.parse("2024-04-04"), "efa146c5-2fa7-11ef-b0b2-0206f94be675") / (double) 20;
    }

    //@Test
    void countBenefitSettlementTest() {
        assertThat(benefitService.countBenefitSettlement(BenefitTargetInfoDTO.builder().academyLocation("가산").startDate(LocalDate.parse("2024-03-03")).endDate(LocalDate.parse("2024-04-04")).courseNumber(277).build())).isEqualTo(19);
    }
}

