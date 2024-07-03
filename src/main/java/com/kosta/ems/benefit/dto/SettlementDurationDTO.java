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
public class SettlementDurationDTO {
    private int settlementDurationSeq;
    private LocalDate settlementDurationStartDate;
    private LocalDate settlementDurationEndDate;
    private int courseSeq;
    private String managerId;
}
