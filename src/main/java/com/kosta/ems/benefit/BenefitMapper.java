package com.kosta.ems.benefit;

import org.apache.ibatis.annotations.Mapper;

import java.time.LocalDate;
import java.util.Collection;

@Mapper
public interface BenefitMapper {
    Collection<BenefitTargetDTO> selectBenefitTarget(String academyLocation, String startDate, String endDate, int courseNumber);
}
