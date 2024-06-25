package com.kosta.ems.scholarship;

import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public interface ScholarshipService {
    Collection<ScholarshipTargetDTO> getScholarshipTargetList(ScholarshipTargetListReqDTO dto, int page, int size);

}
