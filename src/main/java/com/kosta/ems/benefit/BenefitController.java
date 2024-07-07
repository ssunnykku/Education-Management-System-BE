package com.kosta.ems.benefit;

import com.kosta.ems.benefit.dto.BenefitSettlementReqDTO;
import com.kosta.ems.benefit.dto.BenefitSettlementResultDTO;
import com.kosta.ems.benefit.dto.BenefitTargetInfoDTO;
import com.kosta.ems.exception.ErrorResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<Map<String, Object>> getBenefitTargetList(@RequestBody BenefitTargetInfoDTO dto, @RequestParam int page) {
        try {
            log.info("{}", dto);
            dto.setAcademyLocation("가산");
            List<BenefitTargetInfoDTO> result = (ArrayList<BenefitTargetInfoDTO>) benefitService.getBenefitTargetList(dto, page, 10);
            return ResponseEntity.ok(Map.of("result", result));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", new ErrorResult("400", e.getMessage())));
        }
    }

    @PostMapping("/settlement")
    public void setBenefitSettlement(@RequestBody BenefitTargetInfoDTO dto) {
        dto.setAcademyLocation("가산");
        dto.setManagerId("d893bf71-2f8f-11ef-b0b2-0206f94be675");

        benefitService.setBenefitSettlement(dto);
    }

    @PostMapping("/result")
    public Map<String, ArrayList<BenefitSettlementResultDTO>> getBenefitSettlementResult(@RequestBody BenefitTargetInfoDTO dto, @RequestParam int page) {
        dto.setAcademyLocation("가산");
        dto.setManagerId("d893bf71-2f8f-11ef-b0b2-0206f94be675");

        return Map.of("result", (ArrayList<BenefitSettlementResultDTO>) benefitService.getBenefitSettlementResult(dto, page, 10));
    }

    @PostMapping("/count")
    public Map<String, Integer> countSettlementTarget(@RequestBody BenefitTargetInfoDTO dto) {
        dto.setAcademyLocation("가산");
        return Map.of("result", benefitService.countBenefitSettlement(dto));
    }

    @PostMapping("/result/count")
    public Map<String, Integer> countBenefitResult(@RequestBody BenefitTargetInfoDTO dto) {
        dto.setAcademyLocation("가산");

        return Map.of("result", benefitService.countBenefitResult(dto));

    }
}
