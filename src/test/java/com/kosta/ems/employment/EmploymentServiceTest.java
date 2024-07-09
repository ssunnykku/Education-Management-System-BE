package com.kosta.ems.employment;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;


import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@Slf4j
public class EmploymentServiceTest {
	@Autowired
	EmploymentService service;
	
	@Test
	@Transactional
	public void getEmploymentInfoByCourseNumber() {
	    List<EmploymentInfoResponse> list = service.getEmploymentInfoByCourseNumber(277, -1, -1);
	    for (EmploymentInfoResponse dto : list) {
            System.out.println(dto.getName() + "는 취업 " + dto.isEmployeed());
        }
	    
	}

}
