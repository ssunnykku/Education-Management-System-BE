package com.kosta.ems.benefit;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Slf4j
class BenefitServiceImplTest {
    @Autowired
    BenefitServiceImpl benefitService;

    @Test
    void getBenefitTargetListTest() {
        log.info(benefitService.getBenefitTargetList(BenefitTargetInfoDTO.builder().startDate(LocalDate.parse("2024-03-03")).endDate(LocalDate.parse("2024-04-04")).courseNumber(277).lectureDays(20).build(), 1, 10).toString());
    }

    @Test
    void countMealAidTest() {
        int data = benefitService.mealAid(LocalDate.parse("2024-03-03"), LocalDate.parse("2024-04-04"), "efa146c5-2fa7-11ef-b0b2-0206f94be675");
        log.info(String.valueOf(data));
        assertThat(data).isEqualTo(130000);
    }

    @Test
    void trainingAidTest() {
        int data = benefitService.trainingAid(LocalDate.parse("2024-03-03"), LocalDate.parse("2024-04-04"), "efa146c5-2fa7-11ef-b0b2-0206f94be675", 20);
        log.info(String.valueOf(data));
    }

    @Test
    @Transactional
    void setBenefitSettlementDTO() {
        BenefitSettlementDurationDTO benefitSettlementDurationDTO = BenefitSettlementDurationDTO.builder().settlementDurationEndDate(LocalDate.parse("2024-07-30")).settlementDurationStartDate(LocalDate.parse("2024-07-02")).courseSeq(19).managerId("d893bf71-2f8f-11ef-b0b2-0206f94be675").build();
        BenefitDTO benefitDTO = BenefitDTO.builder().trainingAidAmount(200000).mealAidAmount(9500).settlementAidAmount(200000).studentId("efa1441a-2fa7-11ef-b0b2-0206f94be675").settlementDurationSeq(4).build();
        log.info(benefitDTO.toString());
        benefitService.setBenefitSettlement(benefitSettlementDurationDTO, benefitDTO);


    }


}