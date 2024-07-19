package com.kosta.ems.studentCourse;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StudentCourseRepo extends JpaRepository<StudentCourseDTO, Integer>{
    List<StudentCourseDTO> findByCourseSeq(int courseSeq);
    List<StudentCourseDTO> findByStudentId(String StudentId);
    
    Optional<StudentCourseDTO> findByStudentIdAndCourseSeq(String StudentId,int courseSeq);
    
    int countByCourseSeq(int courseSeq);
		
}
