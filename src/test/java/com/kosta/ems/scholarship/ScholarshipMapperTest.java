package com.kosta.ems.scholarship;

import com.kosta.ems.scholarship.dto.ScholarshipTargetDTO;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@Slf4j
@SpringBootTest
class ScholarshipMapperTest {
    @Autowired
    ScholarshipMapper scholarshipMapper;

    @Test
    public void selectScholarshipTargetList() {

        List<ScholarshipTargetDTO> data = (ArrayList<ScholarshipTargetDTO>) scholarshipMapper.selectScholarshipTargetList("", "손", "277", 10, 0);
        log.info(data.toString());
//        assertThat(data).size().isEqualTo(1);
    }

    @Test
    public void selectScholarshipTargetList2() {
        List<ScholarshipTargetDTO> data = (ArrayList<ScholarshipTargetDTO>) scholarshipMapper.selectScholarshipTargetList("", "", "", 10, 0);
        log.info(data.toString());
//        assertThat(data).size().isEqualTo(1);
    }

    @Test
    @Transactional
    public void insertScholarshipSettlementTest() {
        scholarshipMapper.insertScholarshipSettlement(28, "3ddf8577-3eaf-11ef-bd30-0206f94be675");
        log.info(scholarshipMapper.selectScholarshipResultList(282, "가산", "", LocalDate.parse("2024-07-19"), 10, 0).toString());
    }

    @Test
    public void selectScholarshipSettlementResultListTest() {
        log.info(scholarshipMapper.selectScholarshipResultList(null, "가산", "손", LocalDate.parse("2024-06-21"), 10, 0).toString());
    }

    @Test
    void countScholarshipTargetListTest() {
        log.info(String.valueOf(scholarshipMapper.countScholarshipTarget("가산", "", "277")));
    }

    @Test
    void countScholarshipResultTest() {
        //assertThat(scholarshipMapper.countScholarshipResult("", "가산", "", null)).isEqualTo(5);
        log.info("{} ", scholarshipMapper.countScholarshipResult("", "가산", "", null));
    }

}