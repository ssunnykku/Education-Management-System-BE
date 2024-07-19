package com.kosta.ems.api;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.kosta.ems.api.dto.AttendanceHistoryResponse;
import com.kosta.ems.api.dto.PointHistoryResponse;
import com.kosta.ems.api.dto.TakenCourseResponse;
import com.kosta.ems.api.dto.UpdateStudentInfoRequest;
import com.kosta.ems.attendance.AttendanceMapper;
import com.kosta.ems.attendance.AttendanceStudentCourseDTO;
import com.kosta.ems.attendance.AttendanceStudentCourseDTO;
import com.kosta.ems.course.CourseDTO;
import com.kosta.ems.course.CourseMapper;
import com.kosta.ems.student.StudentMapper;
import com.kosta.ems.studentCourse.StudentCourseDTO;
import com.kosta.ems.studentCourse.StudentCourseRepo;
import com.kosta.ems.studentPoint.StudentPointMapper;
import com.kosta.ems.studentPoint.dto.PointHistoryDTO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ApiService {
    private static final int ATTENDANCE_ACK_RATE_PCT = 80;
    private final StudentCourseRepo sCRepo;
    private final CourseMapper courseMapper;
    private final AttendanceMapper attendanceMapper;
    private final StudentPointMapper studentPointMapper;
    private final StudentMapper studentMapper;
    
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

    public List<PointHistoryResponse> getPointHistory(int courseSeq, String studentId) {
        List<PointHistoryResponse> result = new ArrayList<>();
        Optional<StudentCourseDTO> sCDto = sCRepo.findByStudentIdAndCourseSeq(studentId,courseSeq);
        if(sCDto.isEmpty()) {
            //오류제어 코드 추가하기
            return null;
        }
        List<PointHistoryDTO> list = studentPointMapper.getPointHistory(sCDto.get().getSeq());
        list.forEach(item -> result.add(new PointHistoryResponse(item.getSaveDate(), item.getCategoryName(), item.getPoint())));
        
        return result;
    }

    public boolean updateStudentContactInfo(String studentId, UpdateStudentInfoRequest dto) {
        //비밀번호 변경을 포함하지 않는 경우 새 비밀번호는 구 비밀번호와 동일하게 설정.
        String newPassword = (dto.getNewPassword() == null || dto.getNewPassword().isEmpty()) ? dto.getCurrentPassword() : dto.getNewPassword(); 
        return studentMapper.updateStudentContactInfo(studentId, dto.getCurrentPassword(), dto.getNewPassword(), dto.getPhoneNumber(), dto.getBank(), dto.getAccountNumber(), dto.getEmail());
    }
}
