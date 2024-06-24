package com.kosta.ems.scholarship;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ScholarshipTargetDTO {
    private String studentId;
    private String hrdNetId;
    private String name;
    private String bank;
    private String account;
    private String managerId;
    private char isActive;
    private Long studentCourseSeq;
    private Long courseSeq;
    private int courseNumber;
    private String academyLocation;
    private String courseName;
    private LocalDate ScholarshipDate;
    private int totalPoint;
    private int scholarshipAmount;
}
