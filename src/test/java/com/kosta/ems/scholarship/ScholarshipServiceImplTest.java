package com.kosta.ems.scholarship;

import com.kosta.ems.scholarship.dto.ScholarshipSettlementResultDTO;
import com.kosta.ems.scholarship.dto.ScholarshipTargetListReqDTO;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Slf4j
class ScholarshipServiceImplTest {
    @Autowired
    ScholarshipService scholarshipService;

    @Test
    void getScholarshipTargetListTest() {
        ScholarshipTargetListReqDTO dto = ScholarshipTargetListReqDTO.builder().name("").courseNumber(277).academyLocation("가산").build();
        log.info(scholarshipService.getScholarshipTargetList(dto, 1, 10).toString());
        log.info(String.valueOf(scholarshipService.getScholarshipTargetList(dto, 2, 10).size()));
    }

    //@Test
    @Transactional
    void setScholarshipSettlementDateTest() {
        scholarshipService.setScholarshipSettlementDate(9);
    }

    @Test
    void getScholarshipSettlementResultListTest() {

        ArrayList<ScholarshipSettlementResultDTO> list =
                (ArrayList<ScholarshipSettlementResultDTO>) scholarshipService.getScholarshipResultList(ScholarshipTargetListReqDTO.builder()
                        .courseNumber(277).name("손").scholarshipDate(LocalDate.parse("2024-06-21")).build(), 1, 10);

        for (ScholarshipSettlementResultDTO dto : list) {
            assertThat(dto.getName()).contains("손");
        }
    }

    @Test
    @Transactional
    void getScholarshipSettlementResultListTest2() {

        scholarshipService.setScholarshipSettlementDate(66);
        scholarshipService.setScholarshipSettlementDate(60);
        scholarshipService.setScholarshipSettlementDate(63);
        scholarshipService.setScholarshipSettlementDate(57);

        ArrayList<ScholarshipSettlementResultDTO> list = (ArrayList<ScholarshipSettlementResultDTO>) scholarshipService.getScholarshipResultList(ScholarshipTargetListReqDTO.builder().courseNumber(277).name("").scholarshipDate(null).build(), 2, 10);
        log.info(list.toString());
        log.info(String.valueOf(list.size()));

    }

    //@Test
    void getCountTargetListTest() {
        ScholarshipTargetListReqDTO dto = ScholarshipTargetListReqDTO.builder().name("").courseNumber(277).academyLocation("가산").build();
        assertThat(scholarshipService.getCountTarget(dto)).isEqualTo(11);
    }
}