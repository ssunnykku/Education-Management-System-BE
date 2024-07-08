package com.kosta.ems.benefit;

import com.kosta.ems.benefit.dto.BenefitSettlementResultDTO;
import com.kosta.ems.benefit.dto.BenefitTargetInfoDTO;
import com.kosta.ems.exception.ErrorResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/benefits")
@RequiredArgsConstructor
@Slf4j
@RestControllerAdvice
public class BenefitController {

    private final BenefitService benefitService;

    @PostMapping
    public ResponseEntity<Map<String, List<BenefitTargetInfoDTO>>> getBenefitTargetList(@RequestBody BenefitTargetInfoDTO dto) {
        log.info("{}", dto);
        dto.setAcademyLocation("가산");
        List<BenefitTargetInfoDTO> result = (ArrayList<BenefitTargetInfoDTO>) benefitService.getBenefitTargetList(dto);
        return ResponseEntity.ok(Map.of("result", result));

    }

    @PostMapping("/settlement")
    public void setBenefitSettlement(@RequestBody BenefitTargetInfoDTO dto) {
        dto.setAcademyLocation("가산");
        dto.setManagerId("d893bf71-2f8f-11ef-b0b2-0206f94be675");
        benefitService.setBenefitSettlement(dto);
    }

    @PostMapping("/result")
    public ResponseEntity<Map<String, ArrayList<BenefitSettlementResultDTO>>> getBenefitSettlementResult(@RequestBody BenefitTargetInfoDTO dto, @RequestParam int page) {
        dto.setAcademyLocation("가산");
        dto.setManagerId("d893bf71-2f8f-11ef-b0b2-0206f94be675");

        return ResponseEntity.ok(Map.of("result", (ArrayList<BenefitSettlementResultDTO>) benefitService.getBenefitSettlementResult(dto, page, 10)));
    }

    @PostMapping("/count")
    public ResponseEntity<Map<String, Integer>> countSettlementTarget(@RequestBody BenefitTargetInfoDTO dto) {
        dto.setAcademyLocation("가산");
        return ResponseEntity.ok(Map.of("result", benefitService.countBenefitSettlement(dto)));
    }

    @PostMapping("/result/count")
    public ResponseEntity<Map<String, Integer>> countBenefitResult(@RequestBody BenefitTargetInfoDTO dto) {
        dto.setAcademyLocation("가산");

        return ResponseEntity.ok(Map.of("result", benefitService.countBenefitResult(dto)));

    }
}
