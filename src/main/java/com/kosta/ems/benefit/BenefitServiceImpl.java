package com.kosta.ems.benefit;

import com.kosta.ems.attendance.AttendanceServiceImpl;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class BenefitServiceImpl implements BenefitService {

    private final BenefitMapper benefitMapper;
    private final AttendanceServiceImpl attendanceService;
    /*특정 기간의 특정 기수, 특정 이름을 가진 학생들의 수당 정보를 불러오기
    훈련수당 계산 - 20만원 - 총 일수 입력받고 출석률 80%만 훈련수당 제공
    식비 계산 - 입력된 기간의 (출석일수 - (지각+조퇴+외출)/3) x 5000
    정착지원금 - 서울 경기, 인천 이외의 지역 20만원으로 가정*/

    @Override
    @Transactional
    public void setBenefitSettlement(BenefitSettlementDurationDTO benefitSettlementDurationDTO, BenefitDTO benefitDTO) {

        benefitMapper.insertBenefitSettlementDuration(benefitSettlementDurationDTO);
        BenefitDTO benefit = BenefitDTO.builder()
                .trainingAidAmount(benefitDTO.getTrainingAidAmount())
                .mealAidAmount(benefitDTO.getMealAidAmount())
                .settlementAidAmount(benefitDTO.getSettlementAidAmount())
                .studentId(benefitDTO.getStudentId())
                .settlementDurationSeq(benefitSettlementDurationDTO.getSettlementDurationSeq())
                .build();

        log.info(String.valueOf(benefitSettlementDurationDTO.getSettlementDurationSeq()));
        benefitMapper.insertBenefitSettlementAmount(benefit);
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

        for (BenefitTargetDTO BenefitDTO : targetList) {
            benefitTargetList.add(BenefitTargetInfoDTO.builder()
                    .academyLocation("가산")
                    .courseSeq(BenefitDTO.getCourseSeq())
                    .managerId(BenefitDTO.getManagerId())
                    .courseNumber(BenefitDTO.getCourseNumber())
                    .courseName(BenefitDTO.getCourseName())
                    .isActive(BenefitDTO.getIsActive())
                    .studentId(BenefitDTO.getStudentId())
                    .name(BenefitDTO.getName())
                    .hrdNetId(BenefitDTO.getHrdNetId())
                    .bank(BenefitDTO.getBank())
                    .account(BenefitDTO.getAccount())
                    .settlementAidAmount(BenefitDTO.getSettlementAidAmount())
                    .trainingAidAmount(trainingAid(dto.getStartDate(), dto.getEndDate(), BenefitDTO.getStudentId(), dto.getLectureDays()))
                    .mealAidAmount(mealAid(dto.getStartDate(), dto.getEndDate(), BenefitDTO.getStudentId()))
                    .build());
        }

        return benefitTargetList;
    }

    public int mealAid(LocalDate startDate, LocalDate endDate, String studentId) {
        int countAttendance = attendanceService.getNumberOfAttendance(startDate, endDate, studentId);
        int countLeave = (attendanceService.getNumberOfLeave(startDate, endDate, studentId) / 3);
        int attendanceDays = countAttendance - countLeave;
        int mealAid = attendanceDays * 5000;

        return mealAid;

    }

    public int trainingAid(LocalDate startDate, LocalDate endDate, String studentId, int lectureDays) {

        int countAttendance = attendanceService.getNumberOfAttendance(startDate, endDate, studentId);
        int countLeave = (attendanceService.getNumberOfLeave(startDate, endDate, studentId) / 3);
        int attendanceDays = countAttendance - countLeave;

        return attendanceDays / lectureDays >= 0.8 ? 200000 : 0;
    }


}
