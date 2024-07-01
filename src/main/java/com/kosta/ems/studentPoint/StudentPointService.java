package com.kosta.ems.studentPoint;

import org.springframework.stereotype.Service;

import com.kosta.ems.studentPoint.dto.PointCategoryDTO;
import com.kosta.ems.studentPoint.dto.PointHistoryDTO;
import com.kosta.ems.studentPoint.dto.StudentCourseWithPointDTO;

import java.util.List;
import java.util.function.IntPredicate;

public interface StudentPointService {
	List<StudentCourseWithPointDTO> getStudentListWithPoint(int courseNumber, String name, int page, int pageSize, String academyLocationOfManager);

	List<PointHistoryDTO> getPointHistory(int studentCourseSeq, String academyLocationOfManager);

	List<PointCategoryDTO> getPointCategoryList();

	boolean insertStudentPoint(int pointSeq, String managerId, int studentCourseSeq, String academyLocationOfManager);

	int getCountOfStudentWithPoint(int courseNumber, String studentName, String academyLocationOfManager);
	
}
