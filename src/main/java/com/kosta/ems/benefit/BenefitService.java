package com.kosta.ems.benefit;

import com.kosta.ems.benefit.dto.BenefitTargetInfoDTO;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

@Service
public interface BenefitService {

    Collection<BenefitTargetInfoDTO> getBenefitTargetList(BenefitTargetInfoDTO dto);

    void setBenefitSettlement(BenefitTargetInfoDTO benefitTargetInfoDTO);

    List<BenefitTargetInfoDTO> getBenefitSettlementResult(BenefitTargetInfoDTO benefitTargetInfoDTO, int page, int size);

    int countBenefitSettlement(BenefitTargetInfoDTO dto);

    int countBenefitResult(BenefitTargetInfoDTO benefitTargetInfoDTO);


}
