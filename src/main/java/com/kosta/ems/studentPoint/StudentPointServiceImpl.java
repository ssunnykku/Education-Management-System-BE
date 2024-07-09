package com.kosta.ems.studentPoint;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.springframework.stereotype.Service;

import com.kosta.ems.course.CourseDTO;
import com.kosta.ems.course.CourseMapper;
import com.kosta.ems.studentPoint.dto.PointCategoryDTO;
import com.kosta.ems.studentPoint.dto.PointHistoryDTO;
import com.kosta.ems.studentPoint.dto.StudentCourseWithPointDTO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StudentPointServiceImpl implements StudentPointService {
    private final StudentPointMapper mapper;
    private final CourseMapper courseMapper;

    @Override
    public List<StudentCourseWithPointDTO> getStudentListWithPoint(int courseNumber, String name, int page, int pageSize, String academyLocationOfManager) {

        int offset = (page - 1) * pageSize;

        return mapper.getStudentListWithPoint(courseNumber, name, offset, pageSize);
    }

    @Override
    public List<PointHistoryDTO> getPointHistory(int studentCourseSeq, String academyLocationOfManager) {
        //학생정보 보안검사 해야함..
        return mapper.getPointHistory(studentCourseSeq);
    }

    @Override
    public List<PointCategoryDTO> getPointCategoryList() {
        return mapper.getPointCategoryList();
    }

    @Override
    public boolean insertStudentPoint(int pointSeq, String managerId, int studentCourseSeq, String academyLocationOfManager) {
        //학생정보 보안검사 해야함..
        boolean result = false;
        try {
            result = mapper.insertStudentPoint(pointSeq, managerId, studentCourseSeq) == 1;
        } catch (Exception e) {
        }
        return result;
    }

    @Override
    public int getCountOfStudentWithPoint(int courseNumber, String studentName, String academyLocationOfManager) {
//		courseMapper.getCourse course 보안검사 해야함
        return mapper.getCountOfStudentWithPoint(courseNumber, studentName);
    }


}
