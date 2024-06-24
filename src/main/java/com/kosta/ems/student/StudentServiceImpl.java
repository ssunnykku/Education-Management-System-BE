package com.kosta.ems.student;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Log4j2
@Service
@RequiredArgsConstructor
@Transactional
public class StudentServiceImpl implements StudentService {

    private final StudentMapper studentMapper;

    @Override
    public Map<String, Collection> getStudentByName(String name) {
        log.info(studentMapper);
        Collection<StudentCourseInfoDTO> data = studentMapper.selectStudentByName("김선희");

        return null;
    }
    
    @Override
    // public Map<String, Collection> getStudentsByNameOrCourseNumber(String name, int courseNumber) {
    public List<StudentBasicInfoDTO> getStudentsByNameOrCourseNumber(String name, int courseNumber, int page, int size) {
		/*
		 * Map<String, Collection> result = new HashMap<String, Collection>();
		 * result.put("data", studentMapper.findByStudentNameOrCourseNumber(name,
		 * courseNumber)); return result;
		 */
    	// return studentMapper.findByStudentNameOrCourseNumber(name, courseNumber);
    	return studentMapper.findByStudentNameOrCourseNumber(name, courseNumber, page-1, size);
    }
}
