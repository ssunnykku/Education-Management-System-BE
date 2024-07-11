package com.kosta.ems.scholarship.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ScholarshipTargetListReqDTO {
    private String name;
    private int courseSeq;
    private String academyLocation;
    private int limit;
    private int offset;

    /*getScholarshipSettlementResultList + name*/
    private int courseNumber;
    private LocalDate settlementDate;

}
