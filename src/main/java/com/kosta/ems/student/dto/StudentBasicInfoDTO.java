package com.kosta.ems.student.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class StudentBasicInfoDTO {
    private String studentId;
    private int studentCourseSeq;
    private int courseNumber;
    private String hrdNetId;
    private String name;
    private LocalDate birth;
    private String address;
    private String bank;
    private String account;
    private String email;
    private String phoneNumber;
    private char gender;
    private String courseName;
}
