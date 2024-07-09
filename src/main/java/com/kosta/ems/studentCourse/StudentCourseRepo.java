package com.kosta.ems.studentCourse;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StudentCourseRepo extends JpaRepository<StudentCourseDTO, Integer>{
    List<StudentCourseDTO> findByCourseSeq(int courseSeq);
		
}
