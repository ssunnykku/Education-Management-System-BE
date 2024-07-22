package com.kosta.ems.attendance.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AttendanceStatusDTO {
    private Integer courseNumber;
    private String name;
    private String courseName;
    private LocalTime inTime;
    private LocalTime outTime;

}
