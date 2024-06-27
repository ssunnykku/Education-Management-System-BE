package com.kosta.ems.attendance;

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
}
