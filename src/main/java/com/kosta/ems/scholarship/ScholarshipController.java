package com.kosta.ems.scholarship;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/scholarships")
@RequiredArgsConstructor
public class ScholarshipController {

    private final ScholarshipService scholarshipService;

    @GetMapping
    public Map<String, ArrayList<ScholarshipTargetDTO>> getScholarshipSettlementList(@RequestBody ScholarshipTargetListReqDTO dto, @RequestParam int page) {

        return Map.of("result", (ArrayList<ScholarshipTargetDTO>) scholarshipService.getScholarshipTargetList(dto, page, 10));
    }

    @PostMapping("/{studentCourseSeq}")
    public void scholarshipSettlement(@PathVariable int studentCourseSeq) {
        scholarshipService.setScholarshipSettlementDate(studentCourseSeq);
    }

    @GetMapping("/result")
    public Map<String, ArrayList<ScholarshipSettlementResultDTO>> getScholarshipSettlementResultList(@RequestBody ScholarshipTargetListReqDTO dto, @RequestParam int page) {
        return Map.of("result", (ArrayList<ScholarshipSettlementResultDTO>) scholarshipService.getScholarshipSettlementResultList(dto, page, 10));
    }


}
