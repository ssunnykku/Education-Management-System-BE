package com.kosta.ems.scholarship;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Slf4j
class ScholarshipServiceImplTest {
    @Autowired
    ScholarshipService scholarshipService;

    @Test
    void getScholarshipTargetListTest() {
        ScholarshipTargetListReqDTO dto = ScholarshipTargetListReqDTO.builder().name("").courseSeq(19).academyLocation("가산").build();
        log.info(scholarshipService.getScholarshipTargetList(dto, 1, 10).toString());
        log.info(String.valueOf(scholarshipService.getScholarshipTargetList(dto, 2, 10).size()));
    }
}