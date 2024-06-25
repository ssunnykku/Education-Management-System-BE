package com.kosta.ems.student;

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