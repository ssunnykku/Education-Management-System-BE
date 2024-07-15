package com.kosta.ems.attendance;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Array;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;


@Log4j2
@Service
@RequiredArgsConstructor
@Transactional
public class AttendanceServiceImpl implements AttendanceService {
    private final AttendanceMapper attendanceMapper;

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
        List<StudentAttendanceListDTO> attendanceList = attendanceMapper.selectAttendanceIntegratedList(name, courseNumber, academyLocation, ((page*size)-size), size);

        for(int i=0; i<attendanceList.size(); i++) {
            ArrayList tmp = new ArrayList<>(2);
            int countAttendance = attendanceList.get(i).getSumAttendance();
            int countLateness = attendanceList.get(i).getSumLateness();
            int countEarlyLeave = attendanceList.get(i).getSumEarlyLeave();
            int countGoOut = attendanceList.get(i).getSumGoOut();
            int countAbsence = attendanceList.get(i).getSumAbsence();
            int countAcknowledge = attendanceList.get(i).getSumAcknowledge();
            int calcAcknowledgeAbsence =  (int)((countLateness + countEarlyLeave + countGoOut) / 3);
            int totalTrainingDays = attendanceList.get(i).getTotalTrainingDays();

            double attendanceRatio = (double)(countAttendance + countAcknowledge - calcAcknowledgeAbsence - countAbsence) / totalTrainingDays * 100;
            String attendanceRatioFormatted = String.format("%.1f", attendanceRatio) + "%";

            tmp.add(attendanceList.get(i));
            tmp.add(attendanceRatioFormatted);

            item.add(tmp);
        }

        return item;
    }

    /*
    // [출결] - 특정일의 수강생 출석 상태 목록 조회 (for 출결 입력/수정)
    // 경우1 _ 기수+수강생명 입력
    // 검색 결과 개수 가져오기 (for 페이지네이션)
    @Override
    public int getCourseNumberAndStudentNameListAmount(String attendanceDate, String academyLocation, String name, int courseNumber) {
    	int year = Integer.parseInt(attendanceDate.split("-")[0] );
    	int month = Integer.parseInt(attendanceDate.split("-")[1]);
    	int day = Integer.parseInt(attendanceDate.split("-")[2]);
    	
    	return attendanceMapper.selectCourseNumberAndStudentNameListAmount(LocalDate.of(year, month, day), academyLocation, name, courseNumber);
    }
    // 검색 결과 데이터 목록 가져오기
    @Override
    public List<AttendanceListBySearchFilterDTO> getCourseNumberAndStudentNameList(String attendanceDate, String academyLocation, String name, int courseNumber, int page, int size) {
    	int year = Integer.parseInt(attendanceDate.split("-")[0] );
    	int month = Integer.parseInt(attendanceDate.split("-")[1]);
    	int day = Integer.parseInt(attendanceDate.split("-")[2]);
    	
    	return attendanceMapper.selectCourseNumberAndStudentNameList(LocalDate.of(year, month, day), academyLocation, name, courseNumber, ((page*size)-size), size);
    }
    
    // 경우2 _ 기수 또는 수강생명 입력
    // 검색 결과 개수 가져오기 (for 페이지네이션)
    @Override
    public int getCourseNumberOrStudentNameListAmount(String attendanceDate, String academyLocation, String name, int courseNumber) {
    	int year = Integer.parseInt(attendanceDate.split("-")[0] );
    	int month = Integer.parseInt(attendanceDate.split("-")[1]);
    	int day = Integer.parseInt(attendanceDate.split("-")[2]);
    	
    	return attendanceMapper.selectCourseNumberOrStudentNameListAmount(LocalDate.of(year, month, day), academyLocation, name, courseNumber);
    }
    // 검색 결과 데이터 목록 가져오기
    @Override
    public List<AttendanceListBySearchFilterDTO> getCourseNumberOrStudentNameList(String attendanceDate, String academyLocation, String name, int courseNumber, int page, int size) {
    	int year = Integer.parseInt(attendanceDate.split("-")[0] );
    	int month = Integer.parseInt(attendanceDate.split("-")[1]);
    	int day = Integer.parseInt(attendanceDate.split("-")[2]);
    	
    	return attendanceMapper.selectCourseNumberOrStudentNameList(LocalDate.of(year, month, day), academyLocation, name, courseNumber, ((page*size)-size), size);
    }
    
    // 경우3 _ 기수+수강생명 미입력
    // 검색 결과 개수 가져오기 (for 페이지네이션)
    @Override 
    public int getDateAndLocationListAmount(String attendanceDate, String academyLocation, String name, int courseNumber) {
    	int year = Integer.parseInt(attendanceDate.split("-")[0] );
    	int month = Integer.parseInt(attendanceDate.split("-")[1]);
    	int day = Integer.parseInt(attendanceDate.split("-")[2]);
    	
    	return attendanceMapper.selectDateAndLocationListAmount(LocalDate.of(year, month, day), academyLocation, name, courseNumber);
    }
    // 검색 결과 데이터 목록 가져오기
    @Override
    public List<AttendanceListBySearchFilterDTO> getDateAndLocationList(String attendanceDate, String academyLocation, String name, int courseNumber, int page, int size) {
    	int year = Integer.parseInt(attendanceDate.split("-")[0] );
    	int month = Integer.parseInt(attendanceDate.split("-")[1]);
    	int day = Integer.parseInt(attendanceDate.split("-")[2]);
    	
    	return attendanceMapper.selectDateAndLocationList(LocalDate.of(year, month, day), academyLocation, name, courseNumber, ((page*size)-size), size);
    }
     */

    // *0715 출결 입력/수정 페이지 검색 결과 데이터 목록 (경우1~3 하나로)
    @Override
    public int getAttendanceStatusListAmount(String attendanceDate, String academyLocation, String name, int courseNumber){
        int year = Integer.parseInt(attendanceDate.split("-")[0] );
        int month = Integer.parseInt(attendanceDate.split("-")[1]);
        int day = Integer.parseInt(attendanceDate.split("-")[2]);

        return attendanceMapper.selectAttendanceStatusListAmount(LocalDate.of(year, month, day), academyLocation, name, courseNumber);
    }
    @Override
    public List<AttendanceListBySearchFilterDTO> getAttendanceStatusList(String attendanceDate, String academyLocation, String name, int courseNumber, int page, int size){
        List<AttendanceListBySearchFilterDTO> result = new ArrayList<>();
        int year = Integer.parseInt(attendanceDate.split("-")[0] );
        int month = Integer.parseInt(attendanceDate.split("-")[1]);
        int day = Integer.parseInt(attendanceDate.split("-")[2]);

        List<AttendanceListBySearchFilterDTO> attendanceList = attendanceMapper.selectAttendanceStatusList(LocalDate.of(year, month, day), academyLocation, name, courseNumber, (page*size)-size, size);

        return attendanceList;
    }


    // [출결] - 선택한 수강생의 출석 상태 수정
    @Override
    public void updateStudentAttendance(List<RequestStudentAttendanceDTO> dto) {
        for(int i=0; i<dto.size(); i++) {
            int year = Integer.parseInt(dto.get(i).getAttendanceDate().split("-")[0]);
            int month = Integer.parseInt(dto.get(i).getAttendanceDate().split("-")[1]);
            int day = Integer.parseInt(dto.get(i).getAttendanceDate().split("-")[2]);
            String status = null;

            switch(dto.get(i).getAttendanceStatus()) {
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
                case "acknowledge": case "출석 인정":
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

    // [출결 입력]
    // 1. 특정일의 출결 상태가 등록되지 않은 수강생 목록 가져오기
    @Override
    public List<AttendanceListBySearchFilterDTO> getNoAttendanceStatusStudentList(String attendanceDate, String academyLocation) {
        int year = Integer.parseInt(attendanceDate.split("-")[0] );
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

        switch(attendanceStatus) {
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
            case "acknowledge": case "출석 인정":
                status = "출석 인정";
                break;
            default:
                status = "출석";
                break;
        }

        UpdateStudentAttendanceStatusDTO dto = UpdateStudentAttendanceStatusDTO.builder().attendanceStatus(status).attendanceDate(LocalDate.of(year, month, day)).studentCourseSeq(studentCourseSeq).managerId(managerId).build();
        attendanceMapper.insertAttendanceStatus(dto);
    }
}
