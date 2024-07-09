package com.kosta.ems.studentCourse;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StudentCourseService {
    private final StudentCourseRepo repo;
    
    public List<StudentCourseDTO> findByCourseSeq(int courseSeq){
        return repo.findByCourseSeq(courseSeq);
    }
    
    public int countByCourseSeq(int courseSeq) {
        return repo.countByCourseSeq(courseSeq);
    }
}
