package com.kosta.ems.manager;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.kosta.ems.managers.ManagerService;

@SpringBootTest
public class ManagerServiceTest {
	@Autowired
	ManagerService service;
	
	@Test
	@Transactional
	public void loginTest() {
		Map<String, String> map = service.login("EMP0001", "1234");
		assertThat(map.get("managerId")).isEqualTo("d893bf71-2f8f-11ef-b0b2-0206f94be675");
	}
	
}
