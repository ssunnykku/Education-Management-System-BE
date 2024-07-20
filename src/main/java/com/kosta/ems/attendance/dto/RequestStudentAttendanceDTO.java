package com.kosta.ems.attendance.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class RequestStudentAttendanceDTO {
    private String attendanceStatus;
    private String attendanceDate;
    private String studentId;
    private String name;
    private int courseNumber;
    private String academyLocation;
    private int studentCourseSeq;
    // private String acknowledgeDocument;  // 증빙서류 파일 string
}
