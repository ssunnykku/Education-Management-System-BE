package com.kosta.ems.attendance.dto;

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
    private int studentCourseSeq;
    private String attendanceStatus;
    private String studentId;
    private int courseSeq;
}
