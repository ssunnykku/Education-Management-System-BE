package com.kosta.ems.student.dto;

import lombok.*;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class AddStudentBasicInfoDTO {
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
    private int courseNumber;
}