package com.kosta.ems.studentPoint;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.kosta.ems.studentPoint.dto.PointCategoryDTO;
import com.kosta.ems.studentPoint.dto.PointHistoryDTO;
import com.kosta.ems.studentPoint.dto.StudentCourseWithPointDTO;

@SpringBootTest
public class StudentPointMapperTest {
	@Autowired
	StudentPointMapper mapper;
	
	@Test
	@Transactional
	public void getStudentListWithPointTest() {
		StudentCourseWithPointDTO dto = mapper.getStudentListWithPoint(277, "손", 0, 5).get(0);
		assertThat(dto.getName()).isEqualTo("손유철");
	}
	
	@Test
	@Transactional
	public void getTotal() {
		assertThat(mapper.getCountOfStudentWithPoint(277, null)).isEqualTo(4);
	}
	
	@Test
	@Transactional
	public void getPointHistory() {
		List<PointHistoryDTO> history = mapper.getPointHistory(19);
		assertThat(history.size()).isEqualTo(6);
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
		int result = mapper.insertStudentPoint(2, "bd8c73e1-39c9-11ef-aad4-06a5a7b26ae5", 19);
		assertThat(result).isEqualTo(1);
	}

}
