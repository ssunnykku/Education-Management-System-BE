package com.kosta.ems.scholarship;

import com.kosta.ems.benefit.BenefitSettlementResultDTO;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public interface ScholarshipService {
    Collection<ScholarshipTargetDTO> getScholarshipTargetList(ScholarshipTargetListReqDTO dto, int page, int size);

    void setScholarshipSettlementDate(int studentCourseSeq);

    Collection<ScholarshipSettlementResultDTO> getScholarshipSettlementResultList(ScholarshipTargetListReqDTO dto, int page, int size);
    
}
