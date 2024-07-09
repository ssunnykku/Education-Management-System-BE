package com.kosta.ems.employment;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


import org.hibernate.annotations.ColumnDefault;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Getter
@ToString
@Entity(name="employments")
public class EmploymentDTO{
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "employment_seq", updatable = false)
    private int seq;
    
    @Column(name="employment_company", nullable = false)
    private String company;
    
    
    @Column(name="employment_memo", nullable = true)
    private String memo;
    
    @Column(name="student_course_seq", nullable = false)
    private int sCSeq;
    
    @ColumnDefault("false")
    @Column(name="is_active", nullable = false)
    private boolean isActive;
    
    @Column(name="manager_id", nullable = false)
    private String managerId;
    
    
    
    
    
}
