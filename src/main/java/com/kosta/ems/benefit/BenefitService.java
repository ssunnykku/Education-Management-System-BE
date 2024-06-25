package com.kosta.ems.benefit;

import java.util.Collection;

public interface BenefitService {

    Collection<BenefitTargetInfoDTO> getBenefitTargetList(BenefitTargetInfoDTO dto, int limit, int offset);

    void setBenefitSettlement(BenefitSettlementDurationDTO benefitSettlementDurationDTO, BenefitDTO benefitDTO);


}
