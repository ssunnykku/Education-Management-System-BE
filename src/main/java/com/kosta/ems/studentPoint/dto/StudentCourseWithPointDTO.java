package com.kosta.ems.studentPoint.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StudentCourseWithPointDTO {
    private int studentCourseSeq;
    private String courseName;
    private String hrdNetId;
    private int courseNumber;
    private LocalDate courseStartDate;
    private LocalDate courseEndDate;
    private String name;
    private int totalPoint;
    
}
