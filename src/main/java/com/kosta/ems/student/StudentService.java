package com.kosta.ems.student;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Map;

@Service
public interface StudentService {
    Map<String, Collection> getStudentByName(String name);
}
