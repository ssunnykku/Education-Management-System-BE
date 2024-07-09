package com.kosta.ems.manager;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.kosta.ems.manager.ManagerMapper;

import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@Slf4j
public class ManagerMapperTest {
	@Autowired
	ManagerMapper mapper;

	@Test
	public void userDetails() {
	    System.out.println();
	    assertThat(mapper.findByEmployeeNumber("EMP0001").getManagerId()).isEqualTo("bd8c73e1-39c9-11ef-aad4-06a5a7b26ae5");
	}
	

}
