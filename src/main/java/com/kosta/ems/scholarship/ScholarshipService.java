package com.kosta.ems.scholarship;

import com.kosta.ems.scholarship.dto.ScholarshipSettlementResultDTO;
import com.kosta.ems.scholarship.dto.ScholarshipTargetDTO;
import com.kosta.ems.scholarship.dto.ScholarshipTargetListReqDTO;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public interface ScholarshipService {
    Collection<ScholarshipTargetDTO> getScholarshipTargetList(ScholarshipTargetListReqDTO dto, int page, int size);

    void setScholarshipSettlement(Integer studentCourseSeq, String managerId);

    Collection<ScholarshipSettlementResultDTO> getScholarshipResultList(ScholarshipTargetListReqDTO dto, int page, int size);

    int getCountTarget(ScholarshipTargetListReqDTO dto);

    int countSettlementResult(ScholarshipTargetListReqDTO dto);

}
