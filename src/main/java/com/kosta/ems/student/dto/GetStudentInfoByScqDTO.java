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
public class GetStudentInfoByScqDTO {
    private String studentId;
    private String hrdNetId;
    private String name;
    private LocalDate birth;
    private String address;
    private String bank;
    private String account;
    private String phoneNumber;
    private String email;
    private char gender;
    private String managerId;
    private char isActive;
}
