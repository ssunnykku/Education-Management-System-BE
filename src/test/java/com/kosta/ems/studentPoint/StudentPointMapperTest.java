package com.kosta.ems.studentPoint;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class StudentPointMapperTest {
	@Autowired
	StudentPointMapper mapper;
	
	@Test
	public void getStudentListWithPointTest() {
		StudentPointDTO dto = mapper.getStudentListWithPoint(277, "손").get(0);
		assertThat(dto.getName()).isEqualTo("손유철");
	}

}
