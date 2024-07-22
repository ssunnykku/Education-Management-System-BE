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
@ToString
@Builder
public class TakenCourseResponse {
    private String courseName;
    private int courseNumber;
    private String courseType;
    private String subject;
    private int totalTrainingDays;
    private int trainingHoursPerDay;
    private String professorName;
    private LocalDate startDate;
    private LocalDate endDate;
    private int attendanceDays;
    private int status;
    private Integer courseSeq;
}
