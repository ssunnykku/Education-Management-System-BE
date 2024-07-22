package com.kosta.ems.benefit;


import com.kosta.ems.attendance.AttendanceMapper;
import com.kosta.ems.benefit.dto.*;
import com.kosta.ems.benefit.enums.BenefitCategory;
import com.kosta.ems.benefit.enums.Calculation;
import com.kosta.ems.course.CourseMapper;
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
    private final CourseMapper courseMapper;

    @Override
    @Transactional
    public void setBenefitSettlement(BenefitTargetInfoDTO dto) {

        int courseSeq = courseMapper.getCourseByCourseNumber(Integer.parseInt(dto.getCourseNumber())).getCourseSeq();

        int settlementDurationSeq = 0;

        SettlementDurationDTO settlementDurationDTO =
                SettlementDurationDTO.builder()
                        .settlementDurationStartDate(dto.getSettlementDurationStartDate())
                        .settlementDurationEndDate(dto.getSettlementDurationEndDate())
                        .courseSeq(courseSeq)
                        .managerId(dto.getManagerId())
                        .build();


        if (benefitMapper.selectLastSettlementDate(dto.getCourseNumber()) == null || !benefitMapper.selectLastSettlementDate(dto.getCourseNumber()).isAfter(dto.getSettlementDurationStartDate())) {

            // 정산 기간 등록
            benefitMapper.insertBenefitSettlementDuration(settlementDurationDTO);
            settlementDurationSeq = settlementDurationDTO.getSettlementDurationSeq();

            log.info(String.valueOf(settlementDurationSeq));

        } else if (benefitMapper.selectLastSettlementDate(dto.getCourseNumber()).isAfter(dto.getSettlementDurationStartDate()) || benefitMapper.selectLastSettlementDate(dto.getCourseNumber()).isEqual(dto.getSettlementDurationStartDate())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "해당 기수의 정산 대상 기간이 아닙니다.");

        }

        // 지원금 정산 대상 가져오기
        ArrayList<BenefitTargetInfoDTO> targetList = (ArrayList<BenefitTargetInfoDTO>)
                benefitMapper.selectBenefitTarget(
                        dto.getAcademyLocation(),
                        dto.getSettlementDurationStartDate(),
                        dto.getSettlementDurationEndDate(),
                        dto.getCourseNumber(),
                        dto.getName(),
                        null, null);
        log.info("정산 대상 = {}", targetList);

        // 정산 금액 입력
        for (BenefitTargetInfoDTO targetInfo : targetList) {

            int settlementAidAmount = settlementAid(dto.getSettlementDurationStartDate(), dto.getSettlementDurationEndDate(), targetInfo.getStudentId(), dto.getLectureDays());
            int trainingAidAmount = trainingAid(dto.getSettlementDurationStartDate(), dto.getSettlementDurationEndDate(), targetInfo.getStudentId(), dto.getLectureDays());
            int mealAidAmount = mealAid(dto.getSettlementDurationStartDate(), dto.getSettlementDurationEndDate(), targetInfo.getStudentId(), dto.getLectureDays());

            if (trainingAidAmount > 0) {
                // 훈련수당
                benefitMapper.insertBenefitSettlementAmount(BenefitDTO.builder()
                        .amount(trainingAidAmount)
                        .studentId(targetInfo.getStudentId())
                        .settlementDurationSeq(settlementDurationSeq)
                        .managerId(dto.getManagerId())
                        .benefitsCategoriesSeq(BenefitCategory.BENEFIT_CATEGORY_SEQ_TRAINING.getBenefitCategory())
                        .build());
            }
            if (settlementAidAmount > 0) {
                // 정착지원금
                benefitMapper.insertBenefitSettlementAmount(BenefitDTO.builder()
                        .amount(settlementAidAmount)
                        .studentId(targetInfo.getStudentId())
                        .settlementDurationSeq(settlementDurationSeq)
                        .managerId(dto.getManagerId())
                        .benefitsCategoriesSeq(BenefitCategory.BENEFIT_CATEGORY_SEQ_SETTLEMENT.getBenefitCategory())
                        .build());
            }
            if (mealAidAmount > 0) {
                // 식비
                benefitMapper.insertBenefitSettlementAmount(BenefitDTO.builder()
                        .amount(mealAidAmount)
                        .studentId(targetInfo.getStudentId())
                        .settlementDurationSeq(settlementDurationSeq)
                        .managerId(dto.getManagerId())
                        .benefitsCategoriesSeq(BenefitCategory.BENEFIT_CATEGORY_SEQ_MEAL.getBenefitCategory())
                        .build());
            }

        }
    }

    @Override
    public List<BenefitTargetInfoDTO> getBenefitSettlementResult(BenefitTargetInfoDTO dto, int page, int size) {
        int limit = size;
        int offset = size * (page - 1);

        log.info("dto: {}", dto);

        List<BenefitTargetInfoDTO> studentInfo = (ArrayList<BenefitTargetInfoDTO>) benefitMapper.resultList(dto.getAcademyLocation(), dto.getName(), dto.getCourseNumber(), dto.getBenefitSettlementDate(), limit, offset);
        List<BenefitTargetInfoDTO> benefitAmount = (ArrayList<BenefitTargetInfoDTO>) benefitMapper.getBefitAmount(dto.getBenefitSettlementDate());
        List<BenefitTargetInfoDTO> result = new ArrayList<BenefitTargetInfoDTO>();

        for (BenefitTargetInfoDTO data : studentInfo) {
            BenefitTargetInfoDTO newData = null;
            newData = BenefitTargetInfoDTO.builder()
                    .courseSeq(data.getCourseSeq())
                    .studentId(data.getStudentId())
                    .courseNumber(data.getCourseNumber())
                    .hrdNetId(data.getHrdNetId())
                    .name(data.getName())
                    .bank(data.getBank())
                    .account(data.getAccount())
                    .benefitSeq(data.getBenefitSeq())
                    .settlementDurationSeq(data.getSettlementDurationSeq())
                    .settlementDurationStartDate(data.getSettlementDurationStartDate())
                    .settlementDurationEndDate(data.getSettlementDurationEndDate())
                    .benefitsCategoriesSeq(data.getBenefitsCategoriesSeq())
                    .benefitSettlementDate(data.getBenefitSettlementDate())
                    .build();

            for (BenefitTargetInfoDTO category : benefitAmount) {

                if (data.getStudentId().equals(category.getStudentId())) {
                    if (category.getBenefitsCategoriesSeq() == BenefitCategory.BENEFIT_CATEGORY_SEQ_TRAINING.getBenefitCategory()) {
                        newData.setTrainingAidAmount(category.getAmount());
                    } else if (category.getBenefitsCategoriesSeq() == BenefitCategory.BENEFIT_CATEGORY_SEQ_MEAL.getBenefitCategory()) {
                        newData.setMealAidAmount(category.getAmount());
                    } else if (category.getBenefitsCategoriesSeq() == BenefitCategory.BENEFIT_CATEGORY_SEQ_SETTLEMENT.getBenefitCategory()) {
                        newData.setSettlementAidAmount(category.getAmount());
                    }

                }

            }
            int totalAmount = newData.getTrainingAidAmount() + newData.getMealAidAmount() + newData.getSettlementAidAmount();
            newData.setTotalAmount(totalAmount);
            result.add(newData);
        }

        log.info("정산 결과:{} ", result);
        return result;
    }

    @Override
    public List<BenefitTargetInfoDTO> getBenefitTargetList(BenefitTargetInfoDTO dto) {
        try {
            List<BenefitTargetInfoDTO> targetList = (ArrayList<BenefitTargetInfoDTO>) benefitMapper.selectBenefitTarget(
                    dto.getAcademyLocation(),
                    dto.getSettlementDurationStartDate(),
                    dto.getSettlementDurationEndDate(),
                    dto.getCourseNumber(),
                    dto.getName(),
                    null,
                    null);

            List<BenefitTargetInfoDTO> benefitTargetList = new ArrayList<>();

            for (BenefitTargetInfoDTO targetInfo : targetList) {

                int settlementAidAmount = settlementAid(dto.getSettlementDurationStartDate(), dto.getSettlementDurationEndDate(), targetInfo.getStudentId(), dto.getLectureDays());
                int trainingAidAmount = trainingAid(dto.getSettlementDurationStartDate(), dto.getSettlementDurationEndDate(), targetInfo.getStudentId(), dto.getLectureDays());
                int mealAidAmount = mealAid(dto.getSettlementDurationStartDate(), dto.getSettlementDurationEndDate(), targetInfo.getStudentId(), dto.getLectureDays());

                if (settlementAidAmount + trainingAidAmount + mealAidAmount > 0) {
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
                            .settlementAidAmount(settlementAidAmount)
                            .trainingAidAmount(trainingAidAmount)
                            .mealAidAmount(mealAidAmount)
                            .settlementDurationStartDate(dto.getSettlementDurationStartDate())
                            .settlementDurationEndDate(dto.getSettlementDurationEndDate())
                            .lectureDays(dto.getLectureDays())
                            .totalAmount(settlementAidAmount + trainingAidAmount + mealAidAmount)
                            .build();
                    benefitTargetList.add(data);
                }

            }

            if (benefitMapper.selectLastSettlementDate(dto.getCourseNumber()) == null) {
                log.info("benefitTargetList:{} ", benefitTargetList == null);

                return benefitTargetList;

            }

            if (benefitMapper.selectLastSettlementDate(dto.getCourseNumber()).isAfter(dto.getSettlementDurationStartDate()) || benefitMapper.selectLastSettlementDate(dto.getCourseNumber()).isEqual(dto.getSettlementDurationStartDate())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "해당 기수의 정산 대상 기간이 아닙니다.");

            }

            log.info("benefitTargetList:{} ", benefitTargetList);
            return benefitTargetList;
        } catch (ResponseStatusException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }

    }

    public int mealAid(LocalDate startDate, LocalDate endDate, String studentId, int lectureDays) {
        int mealAid = attendanceDays(startDate, endDate, studentId) * (int) Calculation.MEAL_AMOUNT.getValue();
        log.info("mealAid {} ", mealAid);
        log.info("attendanceDays(startDate, endDate, studentId) {}", attendanceDays(startDate, endDate, studentId));
        return attendanceDays(startDate, endDate, studentId) / (double) lectureDays >= Calculation.COMPLETION_RATE.getValue() ? mealAid : 0;

    }

    public int trainingAid(LocalDate startDate, LocalDate endDate, String studentId, int lectureDays) {

        return attendanceDays(startDate, endDate, studentId) / (double) lectureDays >= Calculation.COMPLETION_RATE.getValue() ? (int) Calculation.TRAINING_AMOUNT.getValue() : 0;
    }

    public int settlementAid(LocalDate startDate, LocalDate endDate, String studentId, int lectureDays) {
        if (studentMapper.selectAddressByStudentId(studentId).contains("서울")
                || studentMapper.selectAddressByStudentId(studentId).contains("경기")
                || studentMapper.selectAddressByStudentId(studentId).contains("인천")) {
            return 0;
        }
        return attendanceDays(startDate, endDate, studentId) / (double) lectureDays >= Calculation.COMPLETION_RATE.getValue() ? (int) Calculation.TRAINING_AMOUNT.getValue() : 0;
    }

    public int attendanceDays(LocalDate startDate, LocalDate endDate, String studentId) {
        int countAttendance = attendanceMapper.selectCountAttendance(startDate, endDate, studentId);
        int countLeave = (attendanceMapper.selectCountLeave(startDate, endDate, studentId) / (int) Calculation.LEAVE_CONVERSION_COUNT.getValue());
        return countAttendance - countLeave;
    }

    @Override
    public int countBenefitSettlement(BenefitTargetInfoDTO dto) {
        try {

            log.info("{}", dto);

            if (benefitMapper.selectLastSettlementDate(dto.getCourseNumber()) == null || !benefitMapper.selectLastSettlementDate(dto.getCourseNumber()).isAfter(dto.getSettlementDurationStartDate())) {
                return benefitMapper.countSettlementTarget(
                        dto.getAcademyLocation(),
                        dto.getSettlementDurationStartDate(),
                        dto.getSettlementDurationEndDate(),
                        dto.getCourseNumber(),
                        dto.getName());
            } else if (benefitMapper.selectLastSettlementDate(dto.getCourseNumber()).isAfter(dto.getSettlementDurationStartDate()) || benefitMapper.selectLastSettlementDate(dto.getCourseNumber()).isEqual(dto.getSettlementDurationStartDate())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "해당 기수의 정산 대상 기간이 아닙니다.");

            }

        } catch (ResponseStatusException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
        return 0;
    }

    @Override
    public int countBenefitResult(BenefitTargetInfoDTO dto) {
        return benefitMapper.countSettlementResult(dto.getAcademyLocation(), dto.getName(), dto.getCourseNumber(), dto.getBenefitSettlementDate());
    }

}
