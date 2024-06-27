package com.kosta.ems.benefit;

import lombok.*;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class BenefitTargetInfoDTO {
    private int courseSeq;
    private String managerId;
    private int courseNumber;
    private String courseName;
    private char isActive;
    private String studentId;
    private String name;
    private String hrdNetId;
    private String bank;
    private String account;
    private int settlementAidAmount;
    private int trainingAidAmount;
    private int mealAidAmount;
    /**/
    private String academyLocation;
    private LocalDate startDate;
    private LocalDate endDate;
    private int lectureDays;
}
