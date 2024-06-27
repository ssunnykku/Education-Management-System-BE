package com.kosta.ems.studentPoint;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.kosta.ems.studentPoint.dto.PointCategoryDTO;
import com.kosta.ems.studentPoint.dto.PointHistoryDTO;
import com.kosta.ems.studentPoint.dto.StudentWithPointDTO;

@SpringBootTest
public class StudentPointMapperTest {
	@Autowired
	StudentPointMapper mapper;
	
	@Test
	@Transactional
	public void getStudentListWithPointTest() {
		StudentWithPointDTO dto = mapper.getStudentListWithPoint(277, "손", 0, 5).get(0);
		assertThat(dto.getName()).isEqualTo("손유철");
	}
	
	@Test
	@Transactional
	public void getPointHistory() {
		List<PointHistoryDTO> history = mapper.getPointHistory(1);
		assertThat(history.size()).isEqualTo(34);
	}
	
	@Test
	@Transactional
	public void getPointCategoryList() {
		List<PointCategoryDTO> category = mapper.getPointCategoryList();
		assertThat(category.size()).isEqualTo(10);
	}
	@Test
	@Transactional
	public void insertStudentPoint() {
		int result = mapper.insertStudentPoint(2, "d893c29b-2f8f-11ef-b0b2-0206f94be675", 1);
		assertThat(result).isEqualTo(1);
	}

}
