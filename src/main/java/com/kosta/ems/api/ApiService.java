package com.kosta.ems.api;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.kosta.ems.api.dto.TakenCourseResponse;
import com.kosta.ems.attendance.AttendanceMapper;
import com.kosta.ems.course.CourseDTO;
import com.kosta.ems.course.CourseMapper;
import com.kosta.ems.studentCourse.StudentCourseDTO;
import com.kosta.ems.studentCourse.StudentCourseRepo;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ApiService {
    private static final int ATTENDANCE_ACK_RATE_PCT = 80;
    private final StudentCourseRepo sCRepo;
    private final CourseMapper courseMapper;
    private final AttendanceMapper attendanceMapper;
    
    public List<TakenCourseResponse> getAllTakenCoursesByStudentId(String studentId) {
        List<TakenCourseResponse> result = new ArrayList<>();
        List<StudentCourseDTO> studentCourseList = sCRepo.findByStudentId(studentId);
        List<CourseDTO> courseList = new ArrayList<>();
        studentCourseList.forEach(item -> {
            courseList.add(courseMapper.getCourse(item.getCourseSeq()));
        });
        
        courseList.forEach(course -> {
            int attendanceDays = attendanceMapper.selectCountAttendance(course.getCourseStartDate(), course.getCourseEndDate(), studentId);
            int status = course.getCourseEndDate().isAfter(LocalDate.now()) ? 0 : (100.0 * attendanceDays / course.getTotalTrainingDays()) > ATTENDANCE_ACK_RATE_PCT ? 1 : -1;
            result.add(TakenCourseResponse.builder()
                    .courseName(course.getCourseName())
                    .courseNumber(course.getCourseNumber())
                    .courseType(course.getCourseType())
                    .startDate(course.getCourseStartDate())
                    .endDate(course.getCourseEndDate())
                    .professorName(course.getProfessorName())
                    .totalTrainingDays(course.getTotalTrainingDays())
                    .trainingHoursPerDay(course.getTrainingHoursOfDate())
                    .subject(course.getSubject())
                    .attendanceDays(attendanceDays)
                    .status(status)
                    .build());
        });
        return result;
    }
    
}
