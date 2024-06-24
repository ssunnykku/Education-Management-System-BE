package com.kosta.ems.scholarship;

import com.kosta.ems.student.StudentMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
class ScholarshipMapperTest {
    @Autowired
    ScholarshipMapper scholarshipMapper;

    @Test
    public void selectScholarshipTargetList() {
        log.info(scholarshipMapper.selectScholarshipTargetList("손유철", (long) 18, "강남").toString());
    }

}