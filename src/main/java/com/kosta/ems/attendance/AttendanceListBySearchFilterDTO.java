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
public class AttendanceListBySearchFilterDTO {
	private int courseNumber;
	private String hrdNetId;
	private String name;
	private String academyLocation;
	private String attendanceStatus;
	private LocalDate attendanceDate;
	private int studentCourseSeq;
}
