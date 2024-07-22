package com.kosta.ems.attendance;

import com.amazonaws.services.s3.AmazonS3Client;
import com.kosta.ems.attendance.dto.*;
import com.kosta.ems.student.StudentMapper;
import com.kosta.ems.student.dto.StudentBasicInfoDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.Period;
import java.util.*;


@Log4j2
@Service
@RequiredArgsConstructor
@Transactional
public class AttendanceServiceImpl implements AttendanceService {
    @Autowired
    private AttendanceTimeRepo attendanceTimeRepo;
    private final AttendanceMapper attendanceMapper;
    private final StudentMapper studentMapper;

    @Autowired
    private AmazonS3Client s3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    @Override
    public int getNumberOfAttendance(LocalDate startDate, LocalDate endDate, String studentId) {
        return attendanceMapper.selectCountAttendance(startDate, endDate, studentId);
    }

    @Override
    public int getNumberOfLeave(LocalDate startDate, LocalDate endDate, String studentId) {
        return attendanceMapper.selectCountLeave(startDate, endDate, studentId);
    }

    // [출결] - 수강생 출석 조회 목록 조회
    // 2차 - 경우 1~3을 하나의 쿼리문으로 해결하기
    @Override
    public int getAttendanceIntegratedListAmount(String name, int courseNumber, String academyLocation) {
        List<StudentAttendanceListDTO> attendanceList = attendanceMapper.selectAttendanceIntegratedListAmount(name, courseNumber, academyLocation);
        return attendanceList.size();
    }

    @Override
    public List<ArrayList> getAttendanceIntegratedList(String name, int courseNumber, String academyLocation, int page, int size) {
        List<ArrayList> item = new ArrayList<>();
        List<StudentAttendanceListDTO> attendanceList = attendanceMapper.selectAttendanceIntegratedList(name, courseNumber, academyLocation, ((page * size) - size), size);

        for (int i = 0; i < attendanceList.size(); i++) {
            ArrayList tmp = new ArrayList<>(2);
            int countAttendance = attendanceList.get(i).getSumAttendance();
            int countLateness = attendanceList.get(i).getSumLateness();
            int countEarlyLeave = attendanceList.get(i).getSumEarlyLeave();
            int countGoOut = attendanceList.get(i).getSumGoOut();
            int countAbsence = attendanceList.get(i).getSumAbsence();
            int countAcknowledge = attendanceList.get(i).getSumAcknowledge();
            int calcAcknowledgeAbsence = (int) ((countLateness + countEarlyLeave + countGoOut) / 3);
            int totalTrainingDays = attendanceList.get(i).getTotalTrainingDays();

            double attendanceRatio = (double) (countAttendance + countAcknowledge - calcAcknowledgeAbsence - countAbsence) / totalTrainingDays * 100;
            String attendanceRatioFormatted = String.format("%.1f", attendanceRatio) + "%";

            tmp.add(attendanceList.get(i));
            tmp.add(attendanceRatioFormatted);

            item.add(tmp);
        }

        return item;
    }

    // *0715 출결 입력/수정 페이지 검색 결과 데이터 목록 (경우1~3 하나로)
    @Override
    public int getAttendanceStatusListAmount(String attendanceDate, String academyLocation, String name, int courseNumber) {
        int year = Integer.parseInt(attendanceDate.split("-")[0]);
        int month = Integer.parseInt(attendanceDate.split("-")[1]);
        int day = Integer.parseInt(attendanceDate.split("-")[2]);

        return attendanceMapper.selectAttendanceStatusListAmount(LocalDate.of(year, month, day), academyLocation, name, courseNumber);
    }

    @Override
    public List<AttendanceListBySearchFilterDTO> getAttendanceStatusList(String attendanceDate, String academyLocation, String name, int courseNumber, int page, int size) {
        List<AttendanceListBySearchFilterDTO> result = new ArrayList<>();
        int year = Integer.parseInt(attendanceDate.split("-")[0]);
        int month = Integer.parseInt(attendanceDate.split("-")[1]);
        int day = Integer.parseInt(attendanceDate.split("-")[2]);

        List<AttendanceListBySearchFilterDTO> attendanceList = attendanceMapper.selectAttendanceStatusList(LocalDate.of(year, month, day), academyLocation, name, courseNumber, (page * size) - size, size);

        return attendanceList;
    }


    // [출결]
    // 선택한 수강생의 출석 상태 수정
    @Override
    public void updateStudentAttendance(List<RequestStudentAttendanceDTO> dto) {
        for (int i = 0; i < dto.size(); i++) {
            int year = Integer.parseInt(dto.get(i).getAttendanceDate().split("-")[0]);
            int month = Integer.parseInt(dto.get(i).getAttendanceDate().split("-")[1]);
            int day = Integer.parseInt(dto.get(i).getAttendanceDate().split("-")[2]);
            String status = null;

            switch (dto.get(i).getAttendanceStatus()) {
                case "lateness":
                case "지각":
                    status = "지각";
                    break;
                case "goOut":
                case "외출":
                    status = "외출";
                    break;
                case "absence":
                case "결석":
                    status = "결석";
                    break;
                case "earlyLeave":
                case "조퇴":
                    status = "조퇴";
                    break;
                case "acknowledge":
                case "출석 인정":
                    status = "출석 인정";
                    break;
                default:
                    status = "출석";
                    break;
            }

            UpdateStudentAttendanceStatusDTO tmpDTO = UpdateStudentAttendanceStatusDTO.builder().attendanceStatus(status).attendanceDate(LocalDate.of(year, month, day)).studentCourseSeq(dto.get(i).getStudentCourseSeq()).build();
            attendanceMapper.updateStudentAttendance(tmpDTO);
        }
    }

    // --[출석 인정]
    // --출석 인정 항목 리스트 가져오기
    @Override
    public List<AttendanceAcknowledgeDTO> getAcknowledgeCategoryList(int isActive) {
        List<AttendanceAcknowledgeDTO> categoryList = attendanceMapper.selectAcknowledgeCategoryList(isActive);
        return categoryList;
    }

    // --출석 인정항목*인정일수 적용하여 출결 상태 반영 (update + insert)
    @Override
    public void reflectAcknowledgeAttendanceStatus(RequestAcknowledgeDTO dto) {
        String attendanceStatus = dto.getAttendanceStatus();
        String evidentialDocument = dto.getEvidentialDocuments();
        int acknowledgeSeq = dto.getAcknowledgeSeq();
        String endDateStr = dto.getEndDate();
        String startDateStr = dto.getStartDate();
        int studentCourseSeq = dto.getStudentCourseSeq();

        LocalDate startDate = LocalDate.parse(startDateStr);
        LocalDate endDate = LocalDate.parse(endDateStr);

        int daysBetween = Period.between(startDate, endDate).getDays();

        String managerId = "3ddf8577-3eaf-11ef-bd30-0206f94be675";  // 임시: 가산 매니저 양유진

        if (daysBetween == 0) {
            // 출석 인정일수가 '1'일인 경우는 startDate를 attendanceDate에 할당하여 출결 상태 Update
            attendanceMapper.updateAttendanceAcknowledgeStatus(attendanceStatus, evidentialDocument, acknowledgeSeq, startDate, studentCourseSeq);
        } else if (daysBetween >= 1) {
            // 출석 인정일수가 '2'일 이상인 경우는 startDate 날짜는 updateAttendanceAcknowledgeStatus,
            // endDate - startDate에서 startDate를 제외하고 endDate까지의 날짜는 insertAttendanceAcknowledgeStatus
            // 예: startDate_2024-07-01, endDate_2024-07-04인 경우, 2024-07-01 날짜로 updateAttendanceAcknowledgeStatus 실행, 2024-07-02, 2024-07-03, 2024-07-4 날짜로 insertAttendanceAcknowledgeStatus 실행

            for (int i = 0; i <= daysBetween; i++) {
                LocalDate tmpDate = startDate.plusDays(i);
                if (tmpDate == startDate) {
                    attendanceMapper.updateAttendanceAcknowledgeStatus(attendanceStatus, evidentialDocument, acknowledgeSeq, startDate, studentCourseSeq);
                } else {
                    attendanceMapper.insertAttendanceAcknowledgeStatus(tmpDate, studentCourseSeq, attendanceStatus, managerId, evidentialDocument, acknowledgeSeq);
                }
            }
        }
    }


    // [출결 입력]
    // 1. 특정일의 출결 상태가 등록되지 않은 수강생 목록 가져오기
    @Override
    public List<AttendanceListBySearchFilterDTO> getNoAttendanceStatusStudentList(String attendanceDate, String academyLocation) {
        int year = Integer.parseInt(attendanceDate.split("-")[0]);
        int month = Integer.parseInt(attendanceDate.split("-")[1]);
        int day = Integer.parseInt(attendanceDate.split("-")[2]);

        return attendanceMapper.selectNoAttendanceStatusStudentList(LocalDate.of(year, month, day), academyLocation);
    }

    // 2. 목록의 학생 중 선택한 학생의 출결 상태 등록하기
    @Override
    public void setAttendanceStatus(String attendanceStatus, String attendanceDate, int studentCourseSeq, String managerId) {
        int year = Integer.parseInt(attendanceDate.split("-")[0]);
        int month = Integer.parseInt(attendanceDate.split("-")[1]);
        int day = Integer.parseInt(attendanceDate.split("-")[2]);
        String status = null;

        switch (attendanceStatus) {
            case "lateness":
            case "지각":
                status = "지각";
                break;
            case "goOut":
            case "외출":
                status = "외출";
                break;
            case "absence":
            case "결석":
                status = "결석";
                break;
            case "earlyLeave":
            case "조퇴":
                status = "조퇴";
                break;
            case "acknowledge":
            case "출석 인정":
                status = "출석 인정";
                break;
            default:
                status = "출석";
                break;
        }

        UpdateStudentAttendanceStatusDTO dto = UpdateStudentAttendanceStatusDTO.builder().attendanceStatus(status).attendanceDate(LocalDate.of(year, month, day)).studentCourseSeq(studentCourseSeq).managerId(managerId).build();
        attendanceMapper.insertAttendanceStatus(dto);
    }

    public List<AttendanceStatusDTO> getTimeByAttendanceDate(LocalDate attendanceDate, int courseNumber) {
        List<AttendanceStatusDTO> data = new ArrayList<AttendanceStatusDTO>();

        List<AttendanceTimeDTO> getTimeByDate = attendanceTimeRepo.findByAttendanceTimeIdAttendanceDate(attendanceDate);
        List<StudentBasicInfoDTO> getStudentByCourse = studentMapper.findByStudentNameOrCourseNumberList("", courseNumber, null, null);

        for (int i = 0; i < getStudentByCourse.size(); i++) {
            AttendanceStatusDTO student = AttendanceStatusDTO.builder().courseName(getStudentByCourse.get(i).getCourseName())
                    .courseNumber(getStudentByCourse.get(i).getCourseNumber())
                    .name(getStudentByCourse.get(i).getName()).build();
            for (int j = 0; j < getTimeByDate.size(); j++) {
                if (getStudentByCourse.get(i).getStudentCourseSeq() == getTimeByDate.get(j).getAttendanceTimeId().getStudentCourseSeq()) {
                    student.setInTime(getTimeByDate.get(j).getInTime());
                    student.setOutTime(getTimeByDate.get(j).getOutTime());
                }
            }
            data.add(student);
        }

        return data;
    }


}
