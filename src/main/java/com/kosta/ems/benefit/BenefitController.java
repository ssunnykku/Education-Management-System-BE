package com.kosta.ems.benefit;

import com.kosta.ems.benefit.dto.BenefitTargetInfoDTO;
import com.kosta.ems.manager.ManagerDTO;
import com.kosta.ems.manager.ManagerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/benefits")
@RequiredArgsConstructor
@Slf4j
public class BenefitController {
    @Value("${security.level}")
    private String SECURITY_LEVEL;
    private final ManagerService managerService;
    private final BenefitService benefitService;

    @PostMapping
    public ResponseEntity<Map<String, Object>> getBenefitTargetList(@RequestBody BenefitTargetInfoDTO dto) {
        try {
            log.info("{}", dto);
            ManagerDTO loginUser = getLoginUser();
            String managerId = loginUser.getManagerId();
            String academyLocation = loginUser.getAcademyLocation();

            dto.setAcademyLocation(academyLocation);
            dto.setManagerId(managerId);
            List<BenefitTargetInfoDTO> result = (ArrayList<BenefitTargetInfoDTO>) benefitService.getBenefitTargetList(dto);
            return ResponseEntity.ok(Map.of("result", result));
        } catch (RuntimeException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("result", "error");
            errorResponse.put("status", HttpStatus.BAD_REQUEST.value());
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }


    }

    @PostMapping("/settlement")
    public ResponseEntity<Map<String, Boolean>> setBenefitSettlement(@RequestBody BenefitTargetInfoDTO dto) {
        try {
            ManagerDTO loginUser = getLoginUser();
            String managerId = loginUser.getManagerId();
            String academyLocation = loginUser.getAcademyLocation();

            dto.setAcademyLocation(academyLocation);
            dto.setManagerId(managerId);
            benefitService.setBenefitSettlement(dto);
            return ResponseEntity.ok(Map.of("result", true));
        } catch (ResponseStatusException | NullPointerException e) {
            log.error(e.getMessage());
            return ResponseEntity.ok(Map.of("result", false));
        }

    }

    @PostMapping("/result")
    public ResponseEntity<Map<String, ArrayList<BenefitTargetInfoDTO>>> getBenefitSettlementResult(@RequestBody BenefitTargetInfoDTO dto, @RequestParam int page) {
        ManagerDTO loginUser = getLoginUser();
        String managerId = loginUser.getManagerId();
        String academyLocation = loginUser.getAcademyLocation();

        dto.setAcademyLocation(academyLocation);
        dto.setManagerId(managerId);

        return ResponseEntity.ok(Map.of("result", (ArrayList<BenefitTargetInfoDTO>) benefitService.getBenefitSettlementResult(dto, page, 10)));
    }

    @PostMapping("/count")
    public ResponseEntity<Map<String, Integer>> countSettlementTarget(@RequestBody BenefitTargetInfoDTO dto) {
        ManagerDTO loginUser = getLoginUser();
        String managerId = loginUser.getManagerId();
        String academyLocation = loginUser.getAcademyLocation();

        dto.setAcademyLocation(academyLocation);
        dto.setManagerId(managerId);
        return ResponseEntity.ok(Map.of("result", benefitService.countBenefitSettlement(dto)));
    }

    @PostMapping("/result/count")
    public ResponseEntity<Map<String, Integer>> countBenefitResult(@RequestBody BenefitTargetInfoDTO dto) {
        ManagerDTO loginUser = getLoginUser();
        String managerId = loginUser.getManagerId();
        String academyLocation = loginUser.getAcademyLocation();

        dto.setAcademyLocation(academyLocation);
        dto.setManagerId(managerId);

        return ResponseEntity.ok(Map.of("result", benefitService.countBenefitResult(dto)));

    }

    private ManagerDTO getLoginUser() {
        ManagerDTO loginUser;
        if (SECURITY_LEVEL.equals("OFF")) {
            loginUser = managerService.findByEmployeeNumber("EMP0001");
        } else {
            loginUser = (ManagerDTO) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        }
        return loginUser;
    }
}
