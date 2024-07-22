package com.kosta.ems.studentCourse;

import java.util.List;

import org.springframework.stereotype.Service;

import com.kosta.ems.course.CourseDTO;
import com.kosta.ems.course.CourseMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StudentCourseService {
    private final StudentCourseRepo repo;
    private final CourseMapper courseMapper;
    
    public List<StudentCourseDTO> findByCourseSeq(int courseSeq){
        return repo.findByCourseSeq(courseSeq);
    }
    
    public int countByCourseSeq(int courseSeq) {
        return repo.countByCourseSeq(courseSeq);
    }
    
    public int countByCourseNumber(int courseNumber) {
        CourseDTO course = courseMapper.getCourseByCourseNumber(courseNumber);
        return repo.countByCourseSeq(course.getCourseSeq());
    }
}
