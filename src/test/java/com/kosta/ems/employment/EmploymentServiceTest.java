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
	    List<EmploymentInfoResponse> list = service.getEmploymentInfoByCourseSeq(5, -1, -1);
	    assertThat(list.size()).isEqualTo(5);
	    
	}
	
	@Test
	@Transactional
	public void getEmployeedRate() {
	    assertThat(service.getEmployeedRatePct(5)).isEqualTo(40.0);
	}

}
