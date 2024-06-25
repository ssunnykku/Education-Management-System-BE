package com.kosta.ems.scholarship;

import com.kosta.ems.benefit.BenefitMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class ScholarshipServiceImpl implements ScholarshipService {

    private final ScholarshipMapper scholarshipMapper;

    @Override
    public List<ScholarshipTargetDTO> getScholarshipTargetList(ScholarshipTargetListReqDTO dto, int page, int size) {
        int limit = size;
        int offset = size * (page - 1);

        List<ScholarshipTargetDTO> resultData = new ArrayList<>();
        dto.setLimit(limit);
        dto.setOffset(offset);

        return (ArrayList<ScholarshipTargetDTO>) scholarshipMapper.selectScholarshipTargetList(dto);
    }
}
