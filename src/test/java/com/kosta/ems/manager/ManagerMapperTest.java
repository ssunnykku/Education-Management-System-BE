package com.kosta.ems.manager;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.kosta.ems.managers.ManagerMapper;

import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@Slf4j
public class ManagerMapperTest {
	@Autowired
	ManagerMapper mapper;

	@Test
	@Transactional
	public void loginTest() {
		Map<String, String> map = mapper.login("EMP0001", "1234");
		for (Map.Entry<String, String> entry : map.entrySet()) {
			String key = entry.getKey();
			String val = entry.getValue();
			System.out.println(key + " : " + val);
		}
	}
	

}
