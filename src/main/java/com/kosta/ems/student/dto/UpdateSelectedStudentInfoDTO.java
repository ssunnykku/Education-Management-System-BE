package com.kosta.ems.student.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class UpdateSelectedStudentInfoDTO {
    private String studentId;
    private String name;
    private String address;
    private String bank;
    private String account;
    private String phoneNumber;
    private String email;
    private int courseNumber;
    private int isActive;
}
