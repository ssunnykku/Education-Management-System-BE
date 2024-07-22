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
public class StudentAttendanceListDTO {
    private int courseNumber;
    private String name;
    private String hrdNetId;
    private int totalTrainingDays;
    private int sumAttendance;  // '출석' 합계
    private int sumLateness;  // '지각' 합계
    private int sumGoOut;  // '조퇴' 합계
    private int sumEarlyLeave;  // '외출' 합계
    private int sumAbsence;  // '결석' 합계
    private int sumAcknowledge;  // '출석 인정' 합계
}
