package com.kosta.ems.scholarship;

import com.kosta.ems.scholarship.dto.ScholarshipSettlementResultDTO;
import com.kosta.ems.scholarship.dto.ScholarshipTargetDTO;
import com.kosta.ems.scholarship.dto.ScholarshipTargetListReqDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ScholarshipServiceImpl implements ScholarshipService {

    private final ScholarshipMapper scholarshipMapper;

    @Override
    public List<ScholarshipTargetDTO> getScholarshipTargetList(ScholarshipTargetListReqDTO dto, int page, int size) {
        int limit = size;
        int offset = size * (page - 1);

        dto.setLimit(limit);
        dto.setOffset(offset);


        return (ArrayList<ScholarshipTargetDTO>) scholarshipMapper.selectScholarshipTargetList(dto.getAcademyLocation(), dto.getName(), String.valueOf(dto.getCourseNumber()), limit, offset);
    }

    @Override
    public int getCountTarget(ScholarshipTargetListReqDTO dto) {
        return scholarshipMapper.countScholarshipTarget(dto.getAcademyLocation(), dto.getName(), String.valueOf(dto.getCourseNumber()));
    }

    @Override
    public void setScholarshipSettlement(Integer studentCourseSeq, String managerId) {
        scholarshipMapper.insertScholarshipSettlement(studentCourseSeq, managerId);

    }

    @Override
    public List<ScholarshipSettlementResultDTO> getScholarshipResultList(ScholarshipTargetListReqDTO dto, int page, int size) {
        int limit = size;
        int offset = size * (page - 1);

        log.info("dto: {}", dto);
        log.info("result: {}", scholarshipMapper.selectScholarshipResultList(dto.getCourseNumber(), dto.getAcademyLocation(), dto.getName(), dto.getSettlementDate(), limit, offset));
        log.info("result: {}", scholarshipMapper.selectScholarshipResultList(dto.getCourseNumber(), dto.getAcademyLocation(), dto.getName(), dto.getSettlementDate(), limit, offset).size());
        dto.setLimit(limit);
        dto.setOffset(offset);

        return (ArrayList<ScholarshipSettlementResultDTO>) scholarshipMapper.selectScholarshipResultList(dto.getCourseNumber(), dto.getAcademyLocation(), dto.getName(), dto.getSettlementDate(), limit, offset);
    }

    @Override
    public int countSettlementResult(ScholarshipTargetListReqDTO dto) {
        return scholarshipMapper.countScholarshipResult(String.valueOf(dto.getCourseNumber()), dto.getAcademyLocation(), dto.getName(), dto.getSettlementDate());
    }
}
