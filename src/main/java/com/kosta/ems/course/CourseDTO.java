package com.kosta.ems.course;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CourseDTO {
    private int courseSeq;
    private String managerId;
    private int courseNumber;
    private String academyLocation;
    private String courseName;
    private LocalDate courseStartDate;
    private LocalDate courseEndDate;
    private String subject;
    private String courseType;
    private int totalTrainingDays;
    private int trainingHoursOfDate;
    private String professorName;
    private int maxStudents;
    private char isActive;
    private int courseEndYear;
    
//    public void setIsActive(boolean isActive) {
//    	this.isActive = isActive ? 'T' : 'F';
//    }
//    
//    public boolean getIsActive() {
//    	return isActive == 'T' ? true : false;
//    }
    
}
