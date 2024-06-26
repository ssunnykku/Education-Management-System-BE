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

    @GetMapping
    public Map<String, ArrayList<BenefitTargetInfoDTO>> getBenefitTargetList(@RequestBody BenefitTargetInfoDTO dto, @RequestParam int page) {
        return Map.of("result", (ArrayList<BenefitTargetInfoDTO>) benefitService.getBenefitTargetList(dto, page, 10));
    }

    @PostMapping
    public void setBenefitSettlement(@RequestBody BenefitSettlementReqDTO dto) {
        benefitService.setBenefitSettlement(dto);
    }

    @PostMapping("/result")
    public Map<String, ArrayList<BenefitSettlementResultDTO>> getBenefitSettlementResult(@RequestBody BenefitSettlementReqDTO dto, @RequestParam int page) {
        return Map.of("result", (ArrayList<BenefitSettlementResultDTO>) benefitService.getBenefitSettlementResult(dto, page, 10));
    }
}
