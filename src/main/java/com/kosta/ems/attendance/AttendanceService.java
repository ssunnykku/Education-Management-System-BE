package com.kosta.ems.attendance;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collection;

@Service
public interface AttendanceService {
    int getNumberOfAttendance(LocalDate startDate, LocalDate endDate, String studentId);

    int getNumberOfLeave(LocalDate startDate, LocalDate endDate, String studentId);


    /* 훈련 수당 대상자 :  출석률 80%*/

}
