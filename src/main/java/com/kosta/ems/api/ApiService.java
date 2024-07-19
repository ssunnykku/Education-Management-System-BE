package com.kosta.ems.api;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.kosta.ems.api.dto.AttendanceHistoryResponse;
import com.kosta.ems.api.dto.TakenCourseResponse;
import com.kosta.ems.attendance.AttendanceMapper;
import com.kosta.ems.attendance.AttendanceStudentCourseDTO;
import com.kosta.ems.attendance.AttendanceStudentCourseDTO;
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
    
    public List<AttendanceHistoryResponse> getAttendanceByMonth(LocalDate date, String studentId){
        List<AttendanceHistoryResponse> result = new ArrayList<>();
        LocalDate startDate = LocalDate.of(date.getYear(), date.getMonth(), 1);
        LocalDate endDate = startDate.plusMonths(1).minusDays(1);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        
        Collection<AttendanceStudentCourseDTO> list = attendanceMapper.selectAttendanceByStudentIdAndDuration(startDate.format(formatter), endDate.format(formatter), studentId);
        list.forEach(item -> {
            result.add(new AttendanceHistoryResponse(item.getAttendanceDate(), item.getAttendanceStatus()));
        });
        return result;
    }

    public double getTotalAttendanceRate(String studentId) {
        CourseDTO course = getCurrentCourse(studentId);
        if(course == null) {
            return 0;
        }
        int attendanceDays = attendanceMapper.selectCountAttendance(course.getCourseStartDate(), course.getCourseEndDate(), studentId);
        
        return 100.0 * attendanceDays / course.getTotalTrainingDays();
    }
    
    public CourseDTO getCurrentCourse(String studentId) {
        List<TakenCourseResponse> courses = getAllTakenCoursesByStudentId(studentId);
        //최근 수강과정이 없거나 이미 지난 내역이라면(courses는 정렬되어있음) 빈 값 리턴
        if(courses.isEmpty() || courses.get(0).getEndDate().isBefore(LocalDate.now())) {
            return null;
        }
        TakenCourseResponse course = courses.get(0);
        return CourseDTO.builder()
                .courseName(course.getCourseName())
                .courseNumber(course.getCourseNumber())
                .courseStartDate(course.getStartDate())
                .courseEndDate(course.getEndDate())
                .courseType(course.getCourseType())
                .professorName(course.getProfessorName())
                .trainingHoursOfDate(course.getTrainingHoursPerDay())
                .totalTrainingDays(course.getTotalTrainingDays())
                .subject(course.getSubject())
                .build();
    }
}
