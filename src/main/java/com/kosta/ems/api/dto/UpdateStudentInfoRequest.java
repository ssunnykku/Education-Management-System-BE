package com.kosta.ems.api.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
public class UpdateStudentInfoRequest {
    private String phoneNumber;
    private String bank;
    private String accountNumber;
    private String email;
    private String currentPassword;
    private String newPassword;
}
