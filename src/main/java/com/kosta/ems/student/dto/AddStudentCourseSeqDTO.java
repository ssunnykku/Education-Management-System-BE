package com.kosta.ems.student.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class AddStudentCourseSeqDTO {
    private String hrdNetId;
    private int courseNumber;
}