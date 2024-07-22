package com.kosta.ems.scholarship.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.cglib.core.Local;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ScholarshipSettlementResultDTO {

    private String studentId;
    private String hrdNetId;
    private String name;
    private String bank;
    private String account;
    private String managerId;
    private char isActive;
    private Long studentCourseSeq;
    private Long courseSeq;
    private Integer courseNumber;
    private String courseName;
    private LocalDate settlementDate;
    private int scholarshipAmount;
}
