package com.kosta.ems.studentPoint;

import org.apache.ibatis.annotations.Mapper;

import com.kosta.ems.studentPoint.dto.PointCategoryDTO;
import com.kosta.ems.studentPoint.dto.PointHistoryDTO;
import com.kosta.ems.studentPoint.dto.StudentWithPointDTO;

import java.util.List;

@Mapper
public interface StudentPointMapper {
	List<StudentWithPointDTO> getStudentListWithPoint(int courseNumber, String name, int offset, int pageSize);

	List<PointHistoryDTO> getPointHistory(int studentCourseSeq);

	List<PointCategoryDTO> getPointCategoryList();

	int insertStudentPoint(int pointSeq, String managerId, int studentCourseSeq);
	
	
	
}
