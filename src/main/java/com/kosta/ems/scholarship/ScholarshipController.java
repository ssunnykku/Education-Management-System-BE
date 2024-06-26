package com.kosta.ems.scholarship;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Map;

@RestController
@RequestMapping("/scholarships")
@RequiredArgsConstructor
public class ScholarshipController {

    private final ScholarshipService scholarshipService;

    @PostMapping
    public Map<String, Collection> getScholarshipSettlementList(@RequestBody ScholarshipTargetListReqDTO dto, @PathVariable int page, @PathVariable int size) {

        return Map.of("result", scholarshipService.getScholarshipTargetList(dto, page, size));
    }

    @PostMapping("/settlement")
    public void scholarshipSettlement(@RequestParam int studentCourseSeq) {
    }

    @PostMapping("/result")
    public Map<String, Collection> getScholarshipSettlementResultList(@RequestBody ScholarshipTargetListReqDTO dto, @PathVariable int page, @PathVariable int size) {
        return Map.of("result", scholarshipService.getScholarshipSettlementResultList(dto, page, size));
    }


}
