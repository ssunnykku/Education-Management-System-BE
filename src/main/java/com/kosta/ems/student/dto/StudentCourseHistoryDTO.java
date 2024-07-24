package com.kosta.ems.student.dto;

import lombok.*;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class StudentCourseHistoryDTO {
    private String studentId;
    private int studentCourseSeq;
    private int courseSeq;
    private String courseType;
    private int courseNumber;
    private String courseName;
    private String professorName;
    private LocalDate courseStartDate;
    private LocalDate courseEndDate;
}
