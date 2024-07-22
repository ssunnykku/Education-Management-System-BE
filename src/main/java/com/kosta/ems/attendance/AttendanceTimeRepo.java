package com.kosta.ems.attendance;

import com.kosta.ems.attendance.dto.AttendanceTimeDTO;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface AttendanceTimeRepo extends JpaRepository<AttendanceTimeDTO, AttendanceTimeId> {

    List<AttendanceTimeDTO> findByAttendanceTimeIdAttendanceDate(LocalDate attendanceDate);
}

