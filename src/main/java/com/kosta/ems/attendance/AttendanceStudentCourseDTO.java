package com.kosta.ems.attendance;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AttendanceStudentCourseDTO {
    private LocalDate attendanceDate;
    private Long studentCourseSeq;
    private String attendanceStatus;
    private String studentId;
    private Long courseSeq;
}
