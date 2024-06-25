package com.kosta.ems.attendance;

import org.apache.ibatis.annotations.Mapper;

import java.time.LocalDate;
import java.util.Collection;

@Mapper
public interface AttendanceMapper {
    Collection<AttendanceStudentCourseDTO> selectAttendanceByStudentIdAndDuration(String startDate, String endDate, String studentId);

    int selectCountAttendance(LocalDate startDate, LocalDate endDate, String studentId);

    int selectCountLeave(LocalDate startDate, LocalDate endDate, String studentId);
}
