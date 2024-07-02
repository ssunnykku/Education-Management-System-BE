package com.kosta.ems.scholarship;

import com.kosta.ems.scholarship.dto.ScholarshipSettlementResultDTO;
import com.kosta.ems.scholarship.dto.ScholarshipTargetDTO;
import com.kosta.ems.scholarship.dto.ScholarshipTargetListReqDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Map;

@RestController
@RequestMapping("/scholarships")
@RequiredArgsConstructor
public class ScholarshipController {

    private final ScholarshipService scholarshipService;

    @PostMapping
    public Map<String, ArrayList<ScholarshipTargetDTO>> getScholarshipSettlementList(@RequestBody ScholarshipTargetListReqDTO dto, @RequestParam(defaultValue = "1") int page) {
        dto.setAcademyLocation("가산");

        return Map.of("result", (ArrayList<ScholarshipTargetDTO>) scholarshipService.getScholarshipTargetList(dto, page, 10));
    }

    @PostMapping("/count")
    public Map<String, Integer> countSettlementTarget(@RequestBody ScholarshipTargetListReqDTO dto) {
        dto.setAcademyLocation("가산");

        return Map.of("result", scholarshipService.getCountTarget(dto));
    }

    @PostMapping("/settlement/{studentCourseSeq}")
    public void scholarshipSettlement(@PathVariable int studentCourseSeq) {
        scholarshipService.setScholarshipSettlementDate(studentCourseSeq);
    }

    @PostMapping("/result")
    public Map<String, ArrayList<ScholarshipSettlementResultDTO>> getScholarshipResultList(@RequestBody ScholarshipTargetListReqDTO dto, @RequestParam(defaultValue = "1") int page) {
        dto.setAcademyLocation("가산");
        return Map.of("result", (ArrayList<ScholarshipSettlementResultDTO>) scholarshipService.getScholarshipResultList(dto, page, 10));
    }

    @PostMapping("/result/count")
    public Map<String, Integer> countResult(@RequestBody ScholarshipTargetListReqDTO dto) {
        dto.setAcademyLocation("가산");
        return Map.of("result", scholarshipService.countSettlementResult(dto));
    }


}
