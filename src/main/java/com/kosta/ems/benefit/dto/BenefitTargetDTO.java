package com.kosta.ems.benefit.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BenefitTargetDTO {
    private int courseSeq;
    private String managerId;
    private int courseNumber;
    private String courseName;
    private char isActive;
    private String studentId;
    private String name;
    private String hrdNetId;
    private String bank;
    private String account;
    private int settlementAidAmount;
}
