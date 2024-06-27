package com.kosta.ems.studentPoint;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

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
		StudentWithPointDTO dto = mapper.getStudentListWithPoint(277, "손").get(0);
		assertThat(dto.getName()).isEqualTo("손유철");
	}
	
	@Test
	public void getPointHistory() {
		List<PointHistoryDTO> history = mapper.getPointHistory(1);
		assertThat(history.size()).isEqualTo(34);
	}
	
	@Test
	public void getPointCategoryList() {
		List<PointCategoryDTO> category = mapper.getPointCategoryList();
		assertThat(category.size()).isEqualTo(10);
	}
	

}
