package com.kosta.ems.attendance;

import org.apache.ibatis.annotations.Mapper;

import java.time.LocalDate;
import java.util.Collection;

@Mapper
public interface AttendanceMapper {
    Collection<AttendanceStudentCourseDTO> selectAttendanceByStudentIdAndDuration(String startDate, String endDate, String studentId);

    int selectCountAttendance(LocalDate startDate, LocalDate endDate, String studentId);

    int selectCountLeave(LocalDate startDate, LocalDate endDate, String studentId);
    
    // [출결] - 수강생 출석 조회 목록 조회
    Collection<StudentAttendanceListDTO> selectStudentAttendanceList(String name, int courseNumber);
    
    // [출결] - 선택한 수강생 출석 상태 수정
    void updateStudentAttendance(UpdateStudentAttendanceStatusDTO dto);
}
