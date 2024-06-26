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

    @PostMapping("/{page}")
    public Map<String, Collection> getScholarshipSettlementList(@RequestBody ScholarshipTargetListReqDTO dto, @PathVariable int page) {

        return Map.of("result", scholarshipService.getScholarshipTargetList(dto, page, 10));
    }

    @PostMapping("/settlement/{studentCourseSeq}")
    public void scholarshipSettlement(@PathVariable int studentCourseSeq) {
        scholarshipService.setScholarshipSettlementDate(studentCourseSeq);
    }

    @PostMapping("/result/{page}")
    public Map<String, Collection> getScholarshipSettlementResultList(@RequestBody ScholarshipTargetListReqDTO dto, @PathVariable int page) {
        return Map.of("result", scholarshipService.getScholarshipSettlementResultList(dto, page, 10));
    }


}
