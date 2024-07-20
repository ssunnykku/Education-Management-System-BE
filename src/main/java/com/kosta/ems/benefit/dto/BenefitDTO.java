package com.kosta.ems.benefit.dto;

import jakarta.validation.constraints.NotNull;
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


    @NotNull
    private Integer benefitSeq;
    @NotNull
    private Integer trainingAidAmount;
    @NotNull
    private Integer mealAidAmount;
    @NotNull
    private Integer settlementAidAmount;
    @NotNull
    private LocalDate benefitSettlementDate;
    @NotNull
    private String studentId;
    @NotNull
    private Integer settlementDurationSeq;
    private int amount;
    private Integer benefitsCategoriesSeq;
    private String managerId;


}
