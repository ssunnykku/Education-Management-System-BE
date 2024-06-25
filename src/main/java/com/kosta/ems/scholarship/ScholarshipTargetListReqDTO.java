package com.kosta.ems.scholarship;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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

}
