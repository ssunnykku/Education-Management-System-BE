package com.kosta.ems.attendance;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Slf4j
class AttendanceServiceImplTest {

    @Autowired
    AttendanceService AttendanceServiceImpl;

//    @Test
//    void getAttendanceByStudentIdAndDurationTest() {
//        List<AttendanceStudentCourseDTO> attendance = (ArrayList<AttendanceStudentCourseDTO>) AttendanceServiceImpl.getAttendanceByStudentIdAndDuration("2024-03-03", "2024-04-04", "efa146c5-2fa7-11ef-b0b2-0206f94be675");
//
//        for (AttendanceStudentCourseDTO a : attendance) {
//            log.info(a.getAttendanceStatus());
//        }
//    }

}