package com.kosta.ems.student.dto;

import lombok.*;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class RequestAddStudentBasicInfoDTO {
    private String hrdNetId;
    private String name;
    private String birth;
    private String address;
    private String bank;
    private String account;
    private String phoneNumber;
    private String email;
    private String gender;
    private String managerId;
    private String courseNumber;
}