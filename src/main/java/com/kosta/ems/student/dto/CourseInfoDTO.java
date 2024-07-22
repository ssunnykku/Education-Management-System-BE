package com.kosta.ems.student.dto;

import lombok.*;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class CourseInfoDTO {
    private int courseSeq;
    private int courseNumber;
    private String courseName;
    private LocalDate courseStartDate;
    private LocalDate courseEndDate;
    private String courseType;
}
