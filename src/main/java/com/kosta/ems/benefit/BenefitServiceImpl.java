package com.kosta.ems.benefit;

import com.kosta.ems.attendance.AttendanceMapper;
import com.kosta.ems.attendance.AttendanceServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class BenefitServiceImpl implements BenefitService {

    private final BenefitMapper benefitMapper;
    private final AttendanceMapper attendanceMapper;
    /*특정 기간의 특정 기수, 특정 이름을 가진 학생들의 수당 정보를 불러오기
    훈련수당 계산 - 20만원 - 총 일수 입력받고 출석률 80%만 훈련수당 제공
    식비 계산 - 입력된 기간의 (출석일수 - (지각+조퇴+외출)/3) x 5000
    정착지원금 - 서울 경기, 인천 이외의 지역 20만원으로 가정*/

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

        List<BenefitSettlementResultDTO> result = (ArrayList<BenefitSettlementResultDTO>) benefitMapper.selectBenefitSettlementResult(dto.getName(), dto.getCourseNumber(), dto.getBenefitSettlementDate(), limit, offset);
        return result;
    }

    @Override
    public List<BenefitTargetInfoDTO> getBenefitTargetList(BenefitTargetInfoDTO dto, int page, int size) {
        int limit = size;
        int offset = size * (page - 1);

        List<BenefitTargetDTO> targetList = (ArrayList<BenefitTargetDTO>) benefitMapper.selectBenefitTarget(
                "가산",
                dto.getStartDate(),
                dto.getEndDate(),
                dto.getCourseNumber(),
                limit,
                offset);

        List<BenefitTargetInfoDTO> benefitTargetList = new ArrayList<>();

        for (BenefitTargetDTO targetInfo : targetList) {
            BenefitTargetInfoDTO data = BenefitTargetInfoDTO.builder()
                    .academyLocation("가산")
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
                    .settlementAidAmount(targetInfo.getSettlementAidAmount())
                    .trainingAidAmount(trainingAid(dto.getStartDate(), dto.getEndDate(), targetInfo.getStudentId(), dto.getLectureDays()))
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
        int countAttendance = attendanceMapper.selectCountAttendance(startDate, endDate, studentId);
        int countLeave = (attendanceMapper.selectCountLeave(startDate, endDate, studentId) / 3);
        int attendanceDays = countAttendance - countLeave;
        int mealAid = attendanceDays * 5000;

        return attendanceDays / lectureDays >= 0.8 ? mealAid : 0;

    }

    public int trainingAid(LocalDate startDate, LocalDate endDate, String studentId, int lectureDays) {

        int countAttendance = attendanceMapper.selectCountAttendance(startDate, endDate, studentId);
        int countLeave = (attendanceMapper.selectCountLeave(startDate, endDate, studentId) / 3);
        int attendanceDays = countAttendance - countLeave;

        return attendanceDays / lectureDays >= 0.8 ? 200000 : 0;
    }


}
