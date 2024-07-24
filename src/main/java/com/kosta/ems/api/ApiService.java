package com.kosta.ems.api;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import com.kosta.ems.student.dto.GetStudentInfoByScqDTO;
import com.kosta.ems.student.dto.RegisteredStudentInfoDTO;
import com.kosta.ems.student.dto.StudentBasicInfoDTO;
import com.kosta.ems.student.dto.StudentInfoDTO;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kosta.ems.api.dto.AttendanceHistoryResponse;
import com.kosta.ems.api.dto.PointHistoryResponse;
import com.kosta.ems.api.dto.TakenCourseResponse;
import com.kosta.ems.api.dto.UpdateStudentInfoRequest;
import com.kosta.ems.attendance.AttendanceMapper;
import com.kosta.ems.attendance.dto.AttendanceStudentCourseDTO;
import com.kosta.ems.attendance.dto.AttendanceTimeDTO;
import com.kosta.ems.attendance.AttendanceTimeId;
import com.kosta.ems.attendance.AttendanceTimeRepo;
import com.kosta.ems.attendance.dto.UpdateStudentAttendanceStatusDTO;
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
@Slf4j
public class ApiService {
    private static final LocalTime CLASS_END_TIME = LocalTime.of(17, 50);
    private static final LocalTime CLASS_START_TIME = LocalTime.of(9, 10);
    private static final String STUDENT_MANAGER_ID = "05bfe60d-45c0-11ef-bd30-0206f94be675";
    private static final int ATTENDANCE_ACK_RATE_PCT = 80;
    private final StudentCourseRepo sCRepo;
    private final CourseMapper courseMapper;
    private final AttendanceMapper attendanceMapper;
    private final StudentPointMapper studentPointMapper;
    private final StudentMapper studentMapper;
    private final AttendanceTimeRepo attendanceTimeRepo;

    public List<TakenCourseResponse> getAllTakenCoursesByStudentId(String studentId) {
        List<TakenCourseResponse> result = new ArrayList<>();
        List<StudentCourseDTO> studentCourseList = sCRepo.findByStudentId(studentId);
        List<CourseDTO> courseList = new ArrayList<>();
        studentCourseList.forEach(item -> {
            courseList.add(courseMapper.getCourse(item.getCourseSeq()));
        });

//        log.info("{} ", courseList);

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
                    .courseSeq(course.getCourseSeq())
                    .build());
        });
        result.sort(Comparator.comparing(TakenCourseResponse::getEndDate).reversed());
        return result;
    }

    public List<AttendanceHistoryResponse> getAttendanceByMonth(LocalDate date, String studentId) {
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
        if (course == null) {
            return 0;
        }
        int attendanceDays = attendanceMapper.selectCountAttendance(course.getCourseStartDate(), course.getCourseEndDate(), studentId);

        return 100.0 * attendanceDays / course.getTotalTrainingDays();
    }

    public CourseDTO getCurrentCourse(String studentId) {
        List<TakenCourseResponse> courses = getAllTakenCoursesByStudentId(studentId);
        //최근 수강과정이 없거나 이미 지난 내역이라면(courses는 정렬되어있음) 빈 값 리턴
        if (courses.isEmpty() || courses.get(0).getEndDate().isBefore(LocalDate.now())) {
            return null;
        }
        TakenCourseResponse course = courses.get(0);
        return courseMapper.getCourseByCourseNumber(course.getCourseNumber());
    }

    public List<PointHistoryResponse> getPointHistory(int courseSeq, String studentId) {
        List<PointHistoryResponse> result = new ArrayList<>();
        Optional<StudentCourseDTO> sCDto = sCRepo.findByStudentIdAndCourseSeq(studentId, courseSeq);
        if (sCDto.isEmpty()) {
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
        return studentMapper.updateStudentContactInfo(studentId, dto.getCurrentPassword(), newPassword, dto.getPhoneNumber(), dto.getBank(), dto.getAccountNumber(), dto.getEmail());
    }

    public AttendanceTimeDTO getAttendanceTimeStatus(String studentId) {
        CourseDTO course = getCurrentCourse(studentId);
        if (course == null) {
            return null;
        }
        StudentCourseDTO sCDTO;
        try {
            sCDTO = sCRepo.findByStudentIdAndCourseSeq(studentId, course.getCourseSeq()).orElseThrow(() -> new RuntimeException("수강하는 과정이 없습니다."));
        } catch (Exception e) {
            return null;
        }
        AttendanceTimeId id = new AttendanceTimeId(LocalDate.now(), sCDTO.getSeq());
        Optional<AttendanceTimeDTO> attendanceTimeDTO = attendanceTimeRepo.findById(id);
        AttendanceTimeDTO result = attendanceTimeDTO.orElseGet(() -> new AttendanceTimeDTO(id, null, null));
        return result;
    }

    public boolean addInTime(String studentId) {
        LocalTime now = LocalTime.now(ZoneOffset.of("+09:00"));
            CourseDTO course = getCurrentCourse(studentId);
            if (course == null) {
                return false;
            }
            AttendanceTimeDTO status = getAttendanceTimeStatus(studentId);
            if (status.getInTime() != null) {
                return false;
            }
        StudentCourseDTO sCDTO = sCRepo.findByStudentIdAndCourseSeq(studentId, course.getCourseSeq()).orElseThrow(() -> new RuntimeException("수강하는 과정이 없습니다."));;
        try {
            //먼저 attendance table에 추가
            
            attendanceMapper.insertAttendanceStatus(UpdateStudentAttendanceStatusDTO.builder()
                    .attendanceDate(LocalDate.now())
                    .attendanceStatus("입실")
                    .managerId(STUDENT_MANAGER_ID)
                    .studentCourseSeq(sCDTO.getSeq())
                    .build());
        }catch (Exception e) {
        }

            //후에 attendance_time table 추가
            AttendanceTimeId id = new AttendanceTimeId(LocalDate.now(), sCDTO.getSeq());
            AttendanceTimeDTO inTime = new AttendanceTimeDTO(id, now, null);
            attendanceTimeRepo.save(inTime);
        return true;
    }

    @Transactional
    public boolean addOutTime(String studentId) {
        LocalTime now = LocalTime.now(ZoneOffset.of("+09:00"));
        CourseDTO course = getCurrentCourse(studentId);
        if (course == null) {
            return false;
        }
        AttendanceTimeDTO status = getAttendanceTimeStatus(studentId);
        if (status.getOutTime() != null || status.getInTime() == null) {
            return false;
        }

        StudentCourseDTO sCDTO;
        try {
            sCDTO = sCRepo.findByStudentIdAndCourseSeq(studentId, course.getCourseSeq()).orElseThrow(() -> new RuntimeException("수강하는 과정이 없습니다."));
        } catch (Exception e) {
            return false;
        }
        AttendanceTimeId id = new AttendanceTimeId(LocalDate.now(), sCDTO.getSeq());
        Optional<AttendanceTimeDTO> oriTime = attendanceTimeRepo.findById(id);
        AttendanceTimeDTO outTime = oriTime.orElseThrow(() -> new RuntimeException());
        outTime.setOutTime(now);

        updateAttendanceBasedOnTime(sCDTO.getSeq(), outTime.getInTime(), outTime.getOutTime());

        return attendanceTimeRepo.save(outTime) != null;
    }

    private void updateAttendanceBasedOnTime(int studentCourseSeq, LocalTime inTime, LocalTime outTime) {
        String status = "출석";
        if (inTime.isAfter(CLASS_END_TIME))
            status = "결석";
        else if (inTime.isAfter(CLASS_START_TIME))
            status = "지각";
        else if (outTime.isBefore(CLASS_END_TIME))
            status = "조퇴";

        attendanceMapper.updateStudentAttendance(new UpdateStudentAttendanceStatusDTO(status, LocalDate.now(), studentCourseSeq, STUDENT_MANAGER_ID));
    }

    public StudentInfoDTO getStudentByHrdNetId(String hrdNetId) {

        RegisteredStudentInfoDTO temp = studentMapper.selectRegisteredStudentBasicInfo(hrdNetId);
        return StudentInfoDTO.builder()
                .studentId(temp.getStudentId())
                .name(temp.getName())
                .hrdNetId(hrdNetId)
                .account(temp.getAccount())
                .birth(temp.getBirth())
                .bank(temp.getBank())
                .phoneNumber(temp.getPhoneNumber())
                .email(temp.getEmail())
                .bank(temp.getBank())
                .build();
    }
    public StudentInfoDTO getStudentByStudentId(String studentId) {
        GetStudentInfoByScqDTO temp = studentMapper.selectStudentInfoByStudentId(studentId);
        return StudentInfoDTO.builder()
                .studentId(temp.getStudentId())
                .name(temp.getName())
                .hrdNetId(temp.getHrdNetId())
                .account(temp.getAccount())
                .birth(temp.getBirth())
                .bank(temp.getBank())
                .phoneNumber(temp.getPhoneNumber())
                .email(temp.getEmail())
                .bank(temp.getBank())
                .build();
    }
}
