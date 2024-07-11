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

//    insert into benefits (training_aid_amount,
//                          meal_aid_amount,
//                          settlement_aid_amount,
//                          student_id,
//                          settlement_duration_seq)

    @NotNull
    private Integer benefitSeq;
    @NotNull
//    private Integer trainingAidAmount;
    @NotNull
//    private Integer mealAidAmount;
    @NotNull
//    private Integer settlementAidAmount;
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
