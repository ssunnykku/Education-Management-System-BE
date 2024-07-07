package com.kosta.ems.benefit;


import com.kosta.ems.attendance.AttendanceMapper;
import com.kosta.ems.benefit.dto.*;
import com.kosta.ems.student.StudentMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.ArrayList;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class BenefitServiceImpl implements BenefitService {

    private final BenefitMapper benefitMapper;
    private final AttendanceMapper attendanceMapper;
    private final StudentMapper studentMapper;

    @Override
    @Transactional
    public void setBenefitSettlement(BenefitTargetInfoDTO dto) {

        SettlementDurationDTO settlementDurationDTO =
                SettlementDurationDTO.builder()
                        .settlementDurationStartDate(dto.getSettlementDurationStartDate())
                        .settlementDurationEndDate(dto.getSettlementDurationEndDate())
                        .courseSeq(dto.getCourseSeq())
                        .managerId(dto.getManagerId())
                        .build();
        // 정산 기간 등록
        benefitMapper.insertBenefitSettlementDuration(settlementDurationDTO);

        // 지원금 정산 대상 가져오기
        ArrayList<BenefitTargetDTO> targetList = (ArrayList<BenefitTargetDTO>)
                benefitMapper.selectBenefitTarget(
                        dto.getAcademyLocation(),
                        dto.getSettlementDurationStartDate(),
                        dto.getSettlementDurationEndDate(),
                        dto.getCourseNumber(),
                        dto.getName(),
                        null, null);
        log.info("정산 대상 = {}", targetList);

        // 정산 금액 입력
        for (BenefitTargetDTO targetInfo : targetList) {
            benefitMapper.insertBenefitSettlementAmount(BenefitDTO.builder()
                    .trainingAidAmount(trainingAid(dto.getSettlementDurationStartDate(), dto.getSettlementDurationEndDate(), targetInfo.getStudentId(), dto.getLectureDays()))
                    .mealAidAmount(mealAid(dto.getSettlementDurationStartDate(), dto.getSettlementDurationEndDate(), targetInfo.getStudentId(), dto.getLectureDays()))
                    .settlementAidAmount(settlementAid(dto.getSettlementDurationStartDate(), dto.getSettlementDurationEndDate(), targetInfo.getStudentId(), dto.getLectureDays()))
                    .studentId(targetInfo.getStudentId())
                    .settlementDurationSeq(settlementDurationDTO.getSettlementDurationSeq())
                    .build());
        }
    }

    @Override
    public List<BenefitSettlementResultDTO> getBenefitSettlementResult(BenefitTargetInfoDTO dto, int page, int size) {
        int limit = size;
        int offset = size * (page - 1);

        List<BenefitSettlementResultDTO> result = (ArrayList<BenefitSettlementResultDTO>) benefitMapper.selectBenefitSettlementResult(dto.getAcademyLocation(), dto.getName(), dto.getCourseNumber(), dto.getBenefitSettlementDate(), limit, offset);
        return result;
    }

    @Override
    public List<BenefitTargetInfoDTO> getBenefitTargetList(BenefitTargetInfoDTO dto, int page, int size) {
        try {
            Integer limit = size;
            Integer offset = size * (page - 1);

            List<BenefitTargetDTO> targetList = (ArrayList<BenefitTargetDTO>) benefitMapper.selectBenefitTarget(
                    dto.getAcademyLocation(),
                    dto.getSettlementDurationStartDate(),
                    dto.getSettlementDurationEndDate(),
                    dto.getCourseNumber(),
                    dto.getName(),
                    limit,
                    offset);

            List<BenefitTargetInfoDTO> benefitTargetList = new ArrayList<>();

            for (BenefitTargetDTO targetInfo : targetList) {
                BenefitTargetInfoDTO data = BenefitTargetInfoDTO.builder()
                        .academyLocation(dto.getAcademyLocation())
                        .courseSeq(targetInfo.getCourseSeq())
                        .managerId(targetInfo.getManagerId())
                        .courseNumber(targetInfo.getCourseNumber())
                        .courseName(targetInfo.getCourseName())
                        .isActive(targetInfo.getIsActive())
                        .studentId(targetInfo.getStudentId())
                        .name(targetInfo.getName())
                        .hrdNetId(targetInfo.getHrdNetId())
                        .bank(targetInfo.getBank())
                        .account(targetInfo.getAccount())
                        .settlementAidAmount(settlementAid(dto.getSettlementDurationStartDate(), dto.getSettlementDurationEndDate(), targetInfo.getStudentId(), dto.getLectureDays()))
                        .trainingAidAmount((int) trainingAid(dto.getSettlementDurationStartDate(), dto.getSettlementDurationEndDate(), targetInfo.getStudentId(), dto.getLectureDays()))
                        .mealAidAmount(mealAid(dto.getSettlementDurationStartDate(), dto.getSettlementDurationEndDate(), targetInfo.getStudentId(), dto.getLectureDays()))
                        .settlementDurationStartDate(dto.getSettlementDurationStartDate())
                        .settlementDurationEndDate(dto.getSettlementDurationEndDate())
                        .lectureDays(dto.getLectureDays())
                        .build();
                benefitTargetList.add(data);

            }

            if (benefitMapper.selectLastSettlementDate(dto.getCourseNumber()) == null) {
                return benefitTargetList;

            }

            if (benefitMapper.selectLastSettlementDate(dto.getCourseNumber()).isAfter(dto.getSettlementDurationStartDate()) || benefitMapper.selectLastSettlementDate(dto.getCourseNumber()).isEqual(dto.getSettlementDurationEndDate())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "해당 기수의 정산 대상 기간이 아닙니다.");

            }

            return benefitTargetList;
        } catch (ResponseStatusException e) {
            throw new RuntimeException(e);
        }

    }

    public int mealAid(LocalDate startDate, LocalDate endDate, String studentId, int lectureDays) {
        int mealAid = attendanceDays(startDate, endDate, studentId) * 5000;
        return attendanceDays(startDate, endDate, studentId) / lectureDays >= 0.8 ? mealAid : 0;

    }

    public int trainingAid(LocalDate startDate, LocalDate endDate, String studentId, int lectureDays) {
        return attendanceDays(startDate, endDate, studentId) / (double) lectureDays >= 0.8 ? 200000 : 0;
    }

    public int settlementAid(LocalDate startDate, LocalDate endDate, String studentId, int lectureDays) {
        if (studentMapper.selectAddressByStudentId(studentId).contains("서울")
                || studentMapper.selectAddressByStudentId(studentId).contains("경기")
                || studentMapper.selectAddressByStudentId(studentId).contains("인천")) {
            return 0;
        }
        return attendanceDays(startDate, endDate, studentId) / (double) lectureDays >= 0.8 ? 200000 : 0;
    }

    public int attendanceDays(LocalDate startDate, LocalDate endDate, String studentId) {
        int countAttendance = attendanceMapper.selectCountAttendance(startDate, endDate, studentId);
        int countLeave = (attendanceMapper.selectCountLeave(startDate, endDate, studentId) / 3);
        return countAttendance - countLeave;
    }

    @Override
    public int countBenefitSettlement(BenefitTargetInfoDTO dto) {

        return benefitMapper.countSettlementTarget(
                dto.getAcademyLocation(),
                dto.getSettlementDurationStartDate(),
                dto.getSettlementDurationEndDate(),
                dto.getCourseNumber(),
                dto.getName());
    }

    @Override
    public int countBenefitResult(BenefitTargetInfoDTO dto) {
        return benefitMapper.countSettlementResult(dto.getAcademyLocation(), dto.getName(), dto.getCourseNumber(), dto.getBenefitSettlementDate());
    }

}
