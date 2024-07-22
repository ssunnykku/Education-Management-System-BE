package com.kosta.ems.attendance;

import java.io.Serializable;
import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@Embeddable
public class AttendanceTimeId implements Serializable{
    @Column(name = "attendance_date", updatable = false)
    private LocalDate attendanceDate;
    
    @Column(name = "student_course_seq", updatable = false)
    private int studentCourseSeq;
}
