package com.kosta.ems.student;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class StudentBasicInfoDTO {
    private String studentId;
    private String hrdNetId;
    private int courseNumber;
    private String name;
    private LocalDate birth;
    private String address;
    private String bank;
    private String account;
    private String phoneNumber;
    private String email;
}
