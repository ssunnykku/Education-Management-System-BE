package com.kosta.ems.benefit;

import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public interface BenefitService {

    Collection<BenefitTargetInfoDTO> getBenefitTargetList(BenefitTargetInfoDTO dto, int limit, int offset);

    void setBenefitSettlement(BenefitSettlementDurationDTO benefitSettlementDurationDTO, BenefitDTO benefitDTO);


}
