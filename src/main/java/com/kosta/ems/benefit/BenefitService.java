package com.kosta.ems.benefit;

import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public interface BenefitService {

    Collection<BenefitTargetInfoDTO> getBenefitTargetList(BenefitTargetInfoDTO dto, int page, int size);

    void setBenefitSettlement(BenefitSettlementReqDTO benefitSettlementReqDTO);

    Collection<BenefitSettlementResultDTO> getBenefitSettlementResult(BenefitSettlementReqDTO benefitSettlementReqDTO, int page, int size);

    int countBenefitSettlement(BenefitTargetInfoDTO dto);

    int countBenefitResult(BenefitSettlementReqDTO benefitSettlementReqDTO);
}
