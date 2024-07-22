package com.kosta.ems.attendance.dto;

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
public class AttendanceListBySearchFilterDTO {
    private int courseNumber;
    private String hrdNetId;
    private String name;
    private String academyLocation;
    private String attendanceStatus;
    private LocalDate attendanceDate;
    private int studentCourseSeq;
    private float attendanceRate;  // 추가 _ 출석률
    private float courseProgressRate;  // 추가 _ 과정 진행률
}
