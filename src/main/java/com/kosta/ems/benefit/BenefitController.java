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

    @PostMapping
    public Map<String, ArrayList<BenefitTargetInfoDTO>> getBenefitTargetList(@RequestBody BenefitTargetInfoDTO dto, @RequestParam int page) {
        dto.setAcademyLocation("가산");
        return Map.of("result", (ArrayList<BenefitTargetInfoDTO>) benefitService.getBenefitTargetList(dto, page, 10));
    }

    @PostMapping("/settlement")
    public void setBenefitSettlement(@RequestBody BenefitSettlementReqDTO dto) {
        benefitService.setBenefitSettlement(dto);
    }

    @PostMapping("/result")
    public Map<String, ArrayList<BenefitSettlementResultDTO>> getBenefitSettlementResult(@RequestBody BenefitSettlementReqDTO dto, @RequestParam int page) {
        return Map.of("result", (ArrayList<BenefitSettlementResultDTO>) benefitService.getBenefitSettlementResult(dto, page, 10));
    }

    @PostMapping("/count")
    public Map<String, Integer> countSettlementTargetList(@RequestBody BenefitTargetInfoDTO dto) {
        dto.setAcademyLocation("가산");
        return Map.of("result", benefitService.countBenefitSettlement(dto));
    }
}
