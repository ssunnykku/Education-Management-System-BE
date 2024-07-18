package com.kosta.ems.attendance;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class AttendanceAcknowledgeDTO {
    private int acknowledgeSeq;
    private String category;
    private int acknowledgeDays;
    private boolean isActive;
}
