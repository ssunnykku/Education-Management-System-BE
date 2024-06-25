package com.kosta.ems.benefit;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Slf4j
class BenefitMapperTest {
    @Autowired
    private BenefitMapper benefitMapper;

    @Test
    public void selectBenefitTargetTest() {
        log.info(benefitMapper.selectBenefitTarget("가산", LocalDate.parse("2024-03-03"), LocalDate.parse("2024-04-04"), 277, 10, 0).toString());
    }

    @Test
    @Transactional
    public void insertBenefitSettlementDurationTest() {
        BenefitSettlementDurationDTO build = BenefitSettlementDurationDTO.builder().settlementDurationEndDate(LocalDate.parse("2024-07-30")).settlementDurationStartDate(LocalDate.parse("2024-07-02")).courseSeq(19).managerId("d893bf71-2f8f-11ef-b0b2-0206f94be675").build();
        log.info(build.toString());
        benefitMapper.insertBenefitSettlementDuration(build);
        log.info(String.valueOf(build.getSettlementDurationSeq()));
    }

    @Test
    @Transactional
    public void insertBenefitSettlementAmountTest() {
        BenefitDTO build = BenefitDTO.builder().trainingAidAmount(200000).mealAidAmount(9500).settlementAidAmount(200000).studentId("efa1441a-2fa7-11ef-b0b2-0206f94be675").settlementDurationSeq(4).build();
        benefitMapper.insertBenefitSettlementAmount(build);
    }

}