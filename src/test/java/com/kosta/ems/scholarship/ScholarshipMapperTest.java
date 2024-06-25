package com.kosta.ems.scholarship;

import com.kosta.ems.student.StudentMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

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
        ScholarshipTargetListReqDTO dto = ScholarshipTargetListReqDTO.builder().name("손유철").courseSeq(18).academyLocation("강남").offset(0).limit(10).build();
        List<ScholarshipTargetDTO> data = (ArrayList<ScholarshipTargetDTO>) scholarshipMapper.selectScholarshipTargetList(dto);
        log.info(data.toString());
        assertThat(data).size().isEqualTo(1);
    }

    @Test
    @Transactional
    public void insertScholarshipSettlementDate() {
        scholarshipMapper.insertScholarshipSettlementDate(5);
    }

    @Test
    public void selectScholarshipSettlementResultListTest() {
        log.info(scholarshipMapper.selectScholarshipSettlementResultList(277, "가산", "손", "2024-06-21", 10, 0).toString());
    }

}