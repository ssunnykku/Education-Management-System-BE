package com.kosta.ems.benefit;


import com.kosta.ems.attendance.AttendanceMapper;
import com.kosta.ems.student.StudentMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public void setBenefitSettlement(BenefitSettlementReqDTO benefitSettlementReqDTO) {

        benefitMapper.insertBenefitSettlementDuration(benefitSettlementReqDTO);
        BenefitDTO benefit = BenefitDTO.builder()
                .trainingAidAmount(benefitSettlementReqDTO.getTrainingAidAmount())
                .mealAidAmount(benefitSettlementReqDTO.getMealAidAmount())
                .settlementAidAmount(benefitSettlementReqDTO.getSettlementAidAmount())
                .studentId(benefitSettlementReqDTO.getStudentId())
                .settlementDurationSeq(benefitSettlementReqDTO.getSettlementDurationSeq())
                .build();

        log.info(String.valueOf(benefitSettlementReqDTO.getSettlementDurationSeq()));
        benefitMapper.insertBenefitSettlementAmount(benefit);
    }

    @Override
    public List<BenefitSettlementResultDTO> getBenefitSettlementResult(BenefitSettlementReqDTO dto, int page, int size) {
        int limit = size;
        int offset = size * (page - 1);

        List<BenefitSettlementResultDTO> result = (ArrayList<BenefitSettlementResultDTO>) benefitMapper.selectBenefitSettlementResult(dto.getAcademyLocation(), dto.getName(), dto.getCourseNumber(), dto.getBenefitSettlementDate(), limit, offset);
        return result;
    }

    @Override
    public List<BenefitTargetInfoDTO> getBenefitTargetList(BenefitTargetInfoDTO dto, int page, int size) {
        int limit = size;
        int offset = size * (page - 1);

        List<BenefitTargetDTO> targetList = (ArrayList<BenefitTargetDTO>) benefitMapper.selectBenefitTarget(
                dto.getAcademyLocation(),
                dto.getStartDate(),
                dto.getEndDate(),
                dto.getCourseNumber(),
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
                    .settlementAidAmount(settlementAid(dto.getStartDate(), dto.getEndDate(), targetInfo.getStudentId(), dto.getLectureDays()))
                    .trainingAidAmount((int) trainingAid(dto.getStartDate(), dto.getEndDate(), targetInfo.getStudentId(), dto.getLectureDays()))
                    .mealAidAmount(mealAid(dto.getStartDate(), dto.getEndDate(), targetInfo.getStudentId(), dto.getLectureDays()))
                    .startDate(dto.getStartDate())
                    .endDate(dto.getEndDate())
                    .lectureDays(dto.getLectureDays())
                    .build();
            benefitTargetList.add(data);

        }
        return benefitTargetList;
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
                dto.getStartDate(),
                dto.getEndDate(),
                dto.getCourseNumber());
    }

    @Override
    public int countBenefitResult(BenefitSettlementReqDTO dto) {
        return benefitMapper.countSettlementResult(dto.getAcademyLocation(), dto.getName(), dto.getCourseNumber(), dto.getBenefitSettlementDate());
    }
}
