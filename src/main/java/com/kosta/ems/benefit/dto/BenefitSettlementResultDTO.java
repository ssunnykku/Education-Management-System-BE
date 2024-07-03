package com.kosta.ems.benefit.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BenefitSettlementResultDTO {
    private int courseSeq;
    private String studentId;
    private int courseNumber;
    private String hrdNetId;
    private String name;
    private String bank;
    private String account;
    private int benefitSeq;
    private int trainingAidAmount;
    private int mealAidAmount;
    private int settlement_aid_amount;
    private LocalDate benefitSettlementDate;
    private String settlementDurationSeq;
    private String settlementDurationStartDate;
    private String settlementDurationEndDate;
    private String managerId;
    private String totalAmount;
}
