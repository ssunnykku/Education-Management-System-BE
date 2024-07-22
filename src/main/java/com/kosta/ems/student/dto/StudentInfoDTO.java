package com.kosta.ems.student.dto;

import lombok.*;

import java.time.LocalDate;
import java.util.List;

// 0710 생성 DTO
// 'Student' 정보 return에 기본적으로 이 DTO 사용할 생각..

// 수강생 정보 조회 페이지
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class StudentInfoDTO {
    private String studentId;
    private String hrdNetId;
    private int courseNumber;
    private String name;
    private LocalDate birth;
    private String address;
    private String bank;
    private String account;
    private String email;
    private String phoneNumber;
    private char gender;
    private int isActive;
    private String managerId;
}
