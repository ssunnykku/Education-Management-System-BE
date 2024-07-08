package com.kosta.ems.benefit;

import com.kosta.ems.benefit.dto.*;
import org.apache.ibatis.annotations.Mapper;

import java.time.LocalDate;
import java.util.Collection;

@Mapper
public interface BenefitMapper {
    Collection<BenefitTargetInfoDTO> selectBenefitTarget(String academyLocation, LocalDate startDate, LocalDate endDate, String courseNumber, String name, Integer limit, Integer offset);

    void insertBenefitSettlementDuration(SettlementDurationDTO settlementDurationDTO);

    void insertBenefitSettlementAmount(BenefitDTO benefitDTO);

    Collection<BenefitTargetInfoDTO> selectBenefitSettlementResult(String academyLocation, String name, String courseNumber, LocalDate benefitSettlementDate, int limit, int offset);

    int countSettlementTarget(String academyLocation, LocalDate startDate, LocalDate endDate, String courseNumber, String name);

    int countSettlementResult(String academyLocation, String name, String courseNumber, LocalDate benefitSettlementDate);

    LocalDate selectLastSettlementDate(String courseNumber);

    int selectDurationSeq(int courseSeq);
}
