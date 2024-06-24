package com.kosta.ems.student;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class StudentCourseInfoDTO {
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
    private String isActive;
    private Long studentCourseSeq;
    private Long courseSeq;
}
