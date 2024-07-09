package com.kosta.ems.studentCourse;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

import org.hibernate.annotations.ColumnDefault;

import com.kosta.ems.employment.EmploymentDTO;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Getter
@ToString
@Entity(name="students_courses")
public class StudentCourseDTO{
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "student_course_seq", updatable = false)
    private int seq;
    
    @Column(name="student_id", nullable = false)
    private String studentId;
    
    @Column(name="course_seq", nullable = false)
    private int courseSeq;
    
    @ColumnDefault("false")
    @Column(name="is_active", nullable = false)
    private boolean isActive;
    
    @Column(name="manager_id", nullable = false)
    private String managerId;
}
