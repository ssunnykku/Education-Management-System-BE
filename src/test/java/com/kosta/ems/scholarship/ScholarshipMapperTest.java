package com.kosta.ems.scholarship;

import com.kosta.ems.student.StudentMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

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
        List<ScholarshipTargetDTO> data = (ArrayList<ScholarshipTargetDTO>) scholarshipMapper.selectScholarshipTargetList("손유철", (long) 18, "강남");
        log.info(data.toString());
        assertThat(data).size().isEqualTo(1);
    }

    @Test
    public void insertScholarshipSettlementDate() {
        scholarshipMapper.insertScholarshipSettlementDate((long) 3);
    }

    @Test
    public void selectScholarshipSettlementResultListTest() {
        log.info(scholarshipMapper.selectScholarshipSettlementResultList(277, "가산", "손", "2024-06-21").toString());
    }
}