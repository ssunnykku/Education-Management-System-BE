package com.kosta.ems.student;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@Service
public interface StudentService {
	
    Map<String, Collection> getStudentByName(String name);
    // Collection<StudentBasicInfoDTO> getStudentsByNameOrCourseNumber(String name, int courseNumber);
    // Map<String, Collection> getStudentsByNameOrCourseNumber(String name, int courseNumber);
    // List<StudentBasicInfoDTO> getStudentsByNameOrCourseNumber(String name, int courseNumber);
    List<StudentBasicInfoDTO> getStudentsByNameOrCourseNumber(String name, int courseNumber, int page, int size);
    // List<StudentBasicInfoDTO> getStudentsByNameOrCourseNumber(String name, int courseNumber, Pageable);
}
