package com.kosta.ems.benefit.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BenefitDTO {
    private int benefitSeq;
    private int trainingAidAmount;
    private int mealAidAmount;
    private int settlementAidAmount;
    private LocalDate benefitSettlementDate;
    private String studentId;
    private int settlementDurationSeq;

}
