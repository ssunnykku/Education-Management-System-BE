package com.kosta.ems.attendance.dto;

import com.kosta.ems.attendance.AttendanceTimeId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.time.LocalTime;

import org.hibernate.annotations.ColumnDefault;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Setter
@Getter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "attendance_times")
public class AttendanceTimeDTO {

    public AttendanceTimeDTO(LocalDate attendanceDate, int studentCourseSeq, LocalTime inTime, LocalTime outTime) {
        this.attendanceTimeId = new AttendanceTimeId(attendanceDate, studentCourseSeq);
        this.inTime = inTime;
        this.outTime = outTime;
    }

    @EmbeddedId
    private AttendanceTimeId attendanceTimeId;

    @Column(name = "in_time", nullable = false)
    private LocalTime inTime;

    @Column(name = "out_time", nullable = false)
    private LocalTime outTime;

}
