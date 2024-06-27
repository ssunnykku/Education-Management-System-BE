package com.kosta.ems.studentPoint;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface StudentPointMapper {
	List<StudentPointDTO> getStudentListWithPoint(int courseNumber, String name);
	
}
