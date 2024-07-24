package com.kosta.ems.attendance.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class RequestAcknowledgeDTO {
    private int acknowledgeSeq;
    private int studentCourseSeq;
    private String attendanceStatus;
    private String startDate;
    private String endDate;
    private String evidentialDocuments;
}
