package com.kosta.ems.scholarship;

import com.kosta.ems.manager.ManagerDTO;
import com.kosta.ems.manager.ManagerService;
import com.kosta.ems.scholarship.dto.ScholarshipSettlementResultDTO;
import com.kosta.ems.scholarship.dto.ScholarshipTargetDTO;
import com.kosta.ems.scholarship.dto.ScholarshipTargetListReqDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Map;

@RestController
@RequestMapping("/scholarships")
@RequiredArgsConstructor
@Slf4j
public class ScholarshipController {
    @Value("${security.level}")
    private String SECURITY_LEVEL;
    private final ManagerService managerService;
    private final ScholarshipService scholarshipService;

    @PostMapping
    public Map<String, ArrayList<ScholarshipTargetDTO>> getScholarshipSettlementList(@RequestBody ScholarshipTargetListReqDTO dto, @RequestParam(defaultValue = "1") int page) {
        ManagerDTO loginUser = getLoginUser();
        String managerId = loginUser.getManagerId();
        String academyLocation = loginUser.getAcademyLocation();

        dto.setAcademyLocation(academyLocation);

        return Map.of("result", (ArrayList<ScholarshipTargetDTO>) scholarshipService.getScholarshipTargetList(dto, page, 10));
    }

    @PostMapping("/count")
    public Map<String, Integer> countSettlementTarget(@RequestBody ScholarshipTargetListReqDTO dto) {
        ManagerDTO loginUser = getLoginUser();
        String managerId = loginUser.getManagerId();
        String academyLocation = loginUser.getAcademyLocation();

        dto.setAcademyLocation(academyLocation);

        return Map.of("result", scholarshipService.getCountTarget(dto));
    }

    @PostMapping("/settlement/{studentCourseSeq}")
    public ResponseEntity<Map<String, Boolean>> scholarshipSettlement(@PathVariable int studentCourseSeq) {
        ManagerDTO loginUser = getLoginUser();
        String managerId = loginUser.getManagerId();
        try {
            scholarshipService.setScholarshipSettlement(studentCourseSeq, managerId);
            return ResponseEntity.ok(Map.of("result", true));

        } catch (ResponseStatusException e) {
            log.error(e.getMessage());
            return ResponseEntity.ok(Map.of("result", false));
        }

    }

    @PostMapping("/result")
    public Map<String, ArrayList<ScholarshipSettlementResultDTO>> getScholarshipResultList(@RequestBody ScholarshipTargetListReqDTO dto, @RequestParam(defaultValue = "1") int page) {
        ManagerDTO loginUser = getLoginUser();
        String managerId = loginUser.getManagerId();
        String academyLocation = loginUser.getAcademyLocation();

        dto.setAcademyLocation(academyLocation);
        return Map.of("result", (ArrayList<ScholarshipSettlementResultDTO>) scholarshipService.getScholarshipResultList(dto, page, 10));
    }

    @PostMapping("/result/count")
    public Map<String, Integer> countResult(@RequestBody ScholarshipTargetListReqDTO dto) {
        ManagerDTO loginUser = getLoginUser();
        String managerId = loginUser.getManagerId();
        String academyLocation = loginUser.getAcademyLocation();

        dto.setAcademyLocation(academyLocation);
        return Map.of("result", scholarshipService.countSettlementResult(dto));
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
