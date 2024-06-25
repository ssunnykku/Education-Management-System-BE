package com.kosta.ems.scholarship;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;

@Mapper
public interface ScholarshipMapper {

    Collection<ScholarshipTargetDTO> selectScholarshipTargetList(ScholarshipTargetListReqDTO scholarshipTargetListReqDTO);

    void insertScholarshipSettlementDate(@Param("studentCourseSeq") int studentCourseSeq);

    Collection<ScholarshipSettlementResultDTO> selectScholarshipSettlementResultList(int courseNumber, String academyLocation, String name, String scholarshipDate, int limit, int offset);


}
