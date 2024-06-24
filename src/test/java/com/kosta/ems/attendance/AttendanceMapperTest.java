package com.kosta.ems.attendance;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
class AttendanceMapperTest {

    @Autowired
    private AttendanceMapper attendanceMapper;

    @Test
    public void selectAttendanceByStudentIdTest() {
        /*null 반환중*/
        log.info(attendanceMapper.selectAttendanceByStudentId("2024-03-03", "2024-04-04", "efa146c5-2fa7-11ef-b0b2-0206f94be675").toString());
    }

}