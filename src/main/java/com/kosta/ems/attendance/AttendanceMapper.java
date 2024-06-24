package com.kosta.ems.attendance;

import org.apache.ibatis.annotations.Mapper;

import java.time.LocalDate;
import java.util.Collection;

@Mapper
public interface AttendanceMapper {

    /*null 반환중*/
    Collection<AttendanceStudentCourseDTO> selectAttendanceByStudentId(String startDate, String endDate, String studentId);
}
