package com.kosta.ems.benefit;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Slf4j
class BenefitMapperTest {
    @Autowired
    private BenefitMapper benefitMapper;

    @Test
    public void selectBenefitTargetTest() {
        log.info(benefitMapper.selectBenefitTarget("가산", "2024-03-03", "2024-04-04", 277).toString());
    }

}