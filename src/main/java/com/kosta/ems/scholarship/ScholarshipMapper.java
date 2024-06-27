package com.kosta.ems.scholarship;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.Collection;

@Mapper
public interface ScholarshipMapper {
    Collection<ScholarshipTargetDTO> selectScholarshipTargetList(String academyLocation, String name, String courseNumber, int limit, int offset);

    void insertScholarshipSettlementDate(int studentCourseSeq);

    Collection<ScholarshipSettlementResultDTO> selectScholarshipResultList(String courseNumber, String academyLocation, String name, LocalDate scholarshipDate, int limit, int offset);

    int countScholarshipTarget(String academyLocation, String name, String courseNumber);

}
