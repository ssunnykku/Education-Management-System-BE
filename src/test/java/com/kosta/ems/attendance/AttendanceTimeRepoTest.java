package com.kosta.ems.attendance;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
class AttendanceTimeRepoTest {

    @Autowired
    private AttendanceTimeRepo attendanceTimeRepo;

    @Test
    void findByIdAttendanceDate() {
        log.info("{}", attendanceTimeRepo.findByAttendanceTimeIdAttendanceDate(LocalDate.parse("2024-07-19")));
    }

}