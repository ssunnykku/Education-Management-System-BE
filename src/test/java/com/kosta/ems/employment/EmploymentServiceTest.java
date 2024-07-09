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
	    List<EmploymentInfoResponse> list = service.getEmploymentInfoByCourseSeq(5, -1, -1);
	    assertThat(list.size()).isEqualTo(5);
	    
	}
	
	@Test
	@Transactional
	public void getEmployeedRate() {
	    assertThat(service.getEmployeedRatePct(5)).isEqualTo(40.0);
	}
	
	@Test
	@Transactional
	public void addStatus() {
	    assertThat(service.addEmployeedStatus(new AddEmployeedStatusRequest(19, "테스트회사"), "bd8c73e1-39c9-11ef-aad4-06a5a7b26ae5")).isTrue();
	}
	
	@Test
	@Transactional
	public void editStatus() {
	    assertThat(service.editEmployeedStatus(new EditEmployeedStatusRequest(1, "금호IDT"), "bd8c73e1-39c9-11ef-aad4-06a5a7b26ae5")).isTrue();
	}
	
	@Test
	@Transactional
	public void deleteStatus() {
	    service.deleteEmployeeStatus(1, "bd8c73e1-39c9-11ef-aad4-06a5a7b26ae5");
	}

}
