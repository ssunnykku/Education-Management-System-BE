package com.kosta.ems.employment;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.kosta.ems.course.CourseDTO;
import com.kosta.ems.course.CourseMapper;
import com.kosta.ems.student.GetStudentInfoByScqDTO;
import com.kosta.ems.student.StudentBasicInfoDTO;
import com.kosta.ems.student.StudentMapper;
import com.kosta.ems.studentCourse.StudentCourseDTO;
import com.kosta.ems.studentCourse.StudentCourseRepo;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmploymentService {
    private final EmploymentRepo repo;
    private final StudentCourseRepo sCRepo;
    private final StudentMapper studentMapper;
    private final CourseMapper courseMapper;
    
    //TODO: pageable 기능 구현하기
    public List<EmploymentInfoDTO> getEmploymentInfoByCourseNumber(int courseNumber, int page, int pageSize){
        List<EmploymentInfoDTO> result = new ArrayList<>();
        
        //기수를 seq로 바꾸고
        CourseDTO course = courseMapper.getCourseByCourseNumber(courseNumber);
        //과정에 다니는 학생 seq 목록을 받아오고 
        List<StudentCourseDTO> sCList = sCRepo.findByCourseSeq(course.getCourseSeq());
        //각 학생에 대해 employment 정보를 조사하여 학생정보와 함께 result에 넣는다.
        for (StudentCourseDTO sCDto : sCList) {
            EmploymentInfoDTO infoDto;
            boolean isEmployeed;
            
            //학생seq로 학생정보를 받아오고
            GetStudentInfoByScqDTO studentDto = studentMapper.selectStudentInfoByScq(sCDto.getSeq());
            //또한 고용정보를 받아온다.
            Optional<EmploymentDTO> employmentOptional = repo.findBysCSeq(sCDto.getSeq());
            EmploymentDTO employmentDto;
            if(employmentOptional.isEmpty()) {
                employmentDto = null;
                isEmployeed = false;
            }else {
                employmentDto = employmentOptional.get();
                isEmployeed = true;
            }
            
            EmploymentInfoDTO resultDto = EmploymentInfoDTO.builder()
            .course(course) //course는 반복문 밖에서 한번만 불러옴.
            .employmentInfo(employmentDto)
            .student(studentDto)
            .isEmployeed(isEmployeed)
            .build();
            
            result.add(resultDto);
        }
        return result;
    }


}
