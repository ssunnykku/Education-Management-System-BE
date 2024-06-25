package com.kosta.ems.managers;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ManagerDTO {
    private String manager_id;
    private String name;
    private String password;
    private String position;
    private String academyLocation;
    private String employeeNumber;
    private char is_active;
    
}
