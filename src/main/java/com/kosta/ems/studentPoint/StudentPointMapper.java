package com.kosta.ems.studentPoint;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface StudentPointMapper {
	List<StudentWithPointDTO> getStudentListWithPoint(int courseNumber, String name);

	List<PointHistoryDTO> getPointHistory(int studentCourseSeq);

	List<PointCategoryDTO> getPointCategoryList();
	
	
	
}
