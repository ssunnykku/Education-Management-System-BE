package com.kosta.ems.scholarship.dto;

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
    private int studentCourseSeq;
    private int courseSeq;
    private int courseNumber;
    private String academyLocation;
    private String courseName;
    private LocalDate settlementDate;
    private int totalPoint;
    private int scholarshipAmount;
}
