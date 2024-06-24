package com.kosta.ems.student;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Map;

@Log4j2
@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {

    private StudentMapper studentMapper;

    @Override
    public Map<String, Collection> getStudentByName(String name) {
        log.info(studentMapper);
        Collection<StudentCourseInfoDTO> data = studentMapper.selectStudentByName("김선희");

        return null;
    }
}
