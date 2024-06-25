package com.kosta.ems.scholarship;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.Map;

@RestController
@RequestMapping("/scholarships")
@RequiredArgsConstructor
public class ScholarshipController {

    private final ScholarshipService scholarshipServiceImpl;

//    @GetMapping
//    public Map<String, Collection> getScholarshipSettlementList(@RequestBody ScholarshipTargetListReqDTO dto, int page, int size) {
//        ScholarshipTargetListReqDTO.builder()
//                .name(dto.getName())
//                .
//                .build()
//        return Map.of("result", scholarshipServiceImpl.getScholarshipTargetList());
//    }


}
