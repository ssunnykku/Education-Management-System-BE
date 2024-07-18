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
		assertThat(mapper.getCountOfStudentWithPoint(277, null)).isGreaterThan(1);
		assertThat(mapper.getCountOfStudentWithPoint(-123, null)).isEqualTo(0);
	}
	
	@Test
	@Transactional
	public void getPointHistory() {
		List<PointHistoryDTO> history = mapper.getPointHistory(19);
		assertThat(history.size()).isGreaterThan(1);
		history = mapper.getPointHistory(-12346567);
		assertThat(history.size()).isEqualTo(0);
	}
	
	@Test
	@Transactional
	public void getPointCategoryList() {
		List<PointCategoryDTO> category = mapper.getPointCategoryList();
		assertThat(category.size()).isGreaterThan(0);
	}
	
	@Test
	@Transactional
	public void insertStudentPoint() {
		int result = mapper.insertStudentPoint(2, "3ddf8303-3eaf-11ef-bd30-0206f94be675", 19);
		assertThat(result).isEqualTo(1);
	}

}
