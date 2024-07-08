package com.kosta.ems.benefit.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SettlementDurationDTO {
    @NotNull
    private int settlementDurationSeq;
    @NotNull
    private LocalDate settlementDurationStartDate;
    @NotNull
    private LocalDate settlementDurationEndDate;
    @NotNull
    private int courseSeq;
    @NotNull
    private String managerId;
}
