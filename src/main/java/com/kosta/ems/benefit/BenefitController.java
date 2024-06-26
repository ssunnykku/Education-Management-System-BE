package com.kosta.ems.benefit;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

@RestController
@RequestMapping("/benefits")
@RequiredArgsConstructor
public class BenefitController {

    private final BenefitService benefitService;

    @PostMapping("/{page}")
    public Map<String, ArrayList<BenefitTargetInfoDTO>> getBenefitTargetList(@RequestBody BenefitTargetInfoDTO dto, @PathVariable int page) {
        return Map.of("result", (ArrayList<BenefitTargetInfoDTO>) benefitService.getBenefitTargetList(dto, page, 10));
    }

    @PostMapping("/settlement")
    public void setBenefitSettlement(@RequestBody BenefitSettlementReqDTO dto) {
        benefitService.setBenefitSettlement(dto);
    }

    @PostMapping("/result/{page}")
    public Map<String, ArrayList<BenefitSettlementResultDTO>> getBenefitSettlementResult(@RequestBody BenefitSettlementReqDTO dto, @PathVariable int page) {
        return Map.of("result", (ArrayList<BenefitSettlementResultDTO>) benefitService.getBenefitSettlementResult(dto, page, 10));
    }
}
