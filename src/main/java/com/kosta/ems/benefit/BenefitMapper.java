package com.kosta.ems.benefit;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.cglib.core.Local;

import java.time.LocalDate;
import java.util.Collection;

@Mapper
public interface BenefitMapper {
    Collection<BenefitTargetDTO> selectBenefitTarget(String academyLocation, LocalDate startDate, LocalDate endDate, int courseNumber, int limit, int offset);

    void insertBenefitSettlementDuration(BenefitSettlementDurationDTO settlementDurationDTO);

    void insertBenefitSettlementAmount(BenefitDTO benefitDTO);
}
