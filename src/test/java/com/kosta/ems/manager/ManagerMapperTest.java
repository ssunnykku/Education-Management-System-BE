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
	    assertThat(mapper.findByEmployeeNumber("EMP0001").getManagerId()).isEqualTo("3ddf8303-3eaf-11ef-bd30-0206f94be675");
	}
	

    @Test
    void findByManagerId() {
        ManagerDTO user = mapper.findByManagerId("3ddf8303-3eaf-11ef-bd30-0206f94be675");
        log.info("{}", user);
        assertThat(user.getName()).isEqualTo("손유철");
    }
}
