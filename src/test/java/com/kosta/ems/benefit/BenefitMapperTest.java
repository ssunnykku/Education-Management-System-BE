package com.kosta.ems.benefit;

import com.kosta.ems.benefit.dto.BenefitDTO;
import com.kosta.ems.benefit.dto.BenefitSettlementReqDTO;
import com.kosta.ems.benefit.dto.BenefitSettlementResultDTO;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Slf4j
class BenefitMapperTest {
    @Autowired
    private BenefitMapper benefitMapper;

    @Test
    public void selectBenefitTargetTest() {
        log.info(benefitMapper.selectBenefitTarget("가산", LocalDate.parse("2024-03-03"), LocalDate.parse("2024-04-04"), 277, "손", 10, 0).toString());
        assertThat(benefitMapper.selectBenefitTarget("가산", LocalDate.parse("2024-03-03"), LocalDate.parse("2024-04-04"), 277, "손", 10, 0).size()).isEqualTo(1);
    }

    @Test
    @Transactional
    public void insertBenefitSettlementDurationTest() {
        BenefitSettlementReqDTO build = BenefitSettlementReqDTO.builder().settlementDurationEndDate(LocalDate.parse("2024-07-30")).settlementDurationStartDate(LocalDate.parse("2024-07-02")).courseSeq(19).managerId("d893bf71-2f8f-11ef-b0b2-0206f94be675").build();
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

    @Test
    void selectBenefitSettlementResultPageTest() {
        List<BenefitSettlementResultDTO> dto = (ArrayList<BenefitSettlementResultDTO>) benefitMapper.selectBenefitSettlementResult("가산", "", "", null, 5, 0);
        for (BenefitSettlementResultDTO d : dto) {
            log.info(d.getBenefitSettlementDate().toString());
        }

        assertThat(dto.size()).isEqualTo(5);
    }

    @Test
    void selectBenefitSettlementResultTest() {
        List<BenefitSettlementResultDTO> dto = (ArrayList<BenefitSettlementResultDTO>) benefitMapper.selectBenefitSettlementResult("가산", "", "227", LocalDate.parse("2024-03-21"), 5, 0);
        for (BenefitSettlementResultDTO d : dto) {
            assertThat(d.getBenefitSettlementDate()).isEqualTo(LocalDate.parse("2024-03-21"));
        }
    }

    @Test
    void selectBenefitSettlementResultTest2() {
        List<BenefitSettlementResultDTO> dto = (ArrayList<BenefitSettlementResultDTO>) benefitMapper.selectBenefitSettlementResult("가산", "", "227", LocalDate.parse("2024-03-21"), 5, 0);
        for (BenefitSettlementResultDTO d : dto) {
            assertThat(d.getCourseNumber()).isEqualTo(277);
        }
    }

    //@Test
    void countSettlementTargetTest() {
        assertThat(benefitMapper.countSettlementTarget("가산", LocalDate.parse("2024-03-03"), LocalDate.parse("2024-04-04"), 277, "")).isEqualTo(19);
    }

    @Test
    void countSettlementResultTest() {
        log.info(String.valueOf(benefitMapper.countSettlementResult("가산", "", "", LocalDate.parse("2024-05-21"))));
    }

}