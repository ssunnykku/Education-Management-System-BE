package com.kosta.ems.attendance;

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
public class UpdateStudentAttendanceStatusDTO {
	private String attendanceStatus;
	private LocalDate attendanceDate;
	// private String studentId;
	// 0703 추가
	private int studentCourseSeq;
}
