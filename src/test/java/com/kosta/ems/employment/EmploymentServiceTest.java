package com.kosta.ems.employment;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.kosta.ems.employment.dto.AddEmployeedStatusRequest;
import com.kosta.ems.employment.dto.EditEmployeedStatusRequest;

import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@Slf4j
public class EmploymentServiceTest {
    @Autowired
    EmploymentService service;

    @Autowired
    EmploymentRepo repo;

    @Test
    @Transactional
    public void getEmploymentInfoByCourseNumber() {
        List<EmploymentInfoResponse> list = service.getEmploymentInfoByCourseSeq(5);
        assertThat(list.size()).isGreaterThan(0);
        
        list = service.getEmploymentInfoByCourseSeq(-5);
        assertThat(list.size()).isEqualTo(0);

    }

    @Test
    @Transactional
    public void getEmployeedRate() {
        assertThat(service.getEmployeedRatePct(277)).isGreaterThan(0);
    }

    @Test
    @Transactional
    public void addStatus() {
        assertThat(service.editEmployeedStatus(new EditEmployeedStatusRequest(27, "금호IDT"),
                "3ddf8303-3eaf-11ef-bd30-0206f94be675")).isTrue();
    }

    @Test
    @Transactional
    public void editStatus() {
        assertThat(service.editEmployeedStatus(new EditEmployeedStatusRequest(27, "금호IDT"),
                "3ddf8303-3eaf-11ef-bd30-0206f94be675")).isTrue();
    }

    @Test
    @Transactional
    public void countEmployeementFindByCourseNumber() {
        assertThat(service.countEmployeedByCourseNumber(277)).isGreaterThan(0);
    }

}
