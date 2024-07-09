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
    public List<EmploymentInfoResponse> getEmploymentInfoByCourseSeq(int courseSeq, int page, int pageSize){
        List<EmploymentInfoResponse> result = new ArrayList<>();
        
        CourseDTO course = courseMapper.getCourse(courseSeq);
        //과정에 다니는 학생 seq 목록을 받아오고 
        List<StudentCourseDTO> sCList = sCRepo.findByCourseSeq(courseSeq);
        //각 학생의 employment 정보와 학생 기본정보를 result에 넣는다.
        for (StudentCourseDTO sCDto : sCList) {
            EmploymentInfoResponse infoDto;
            String company = null;
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
                isEmployeed = true;
                company = employmentOptional.get().getCompany();
            }
            
            EmploymentInfoResponse resultDto = EmploymentInfoResponse.builder()
            .sCSeq(sCDto.getSeq())
            .hrdNetId(studentDto.getHrdNetId())
            .courseNumber(course.getCourseNumber())
            .name(studentDto.getName())
            .phoneNumber(studentDto.getPhoneNumber())
            .email(studentDto.getEmail())
            .courseEndDate(course.getCourseEndDate())
            .company(company)
            .isEmployeed(isEmployeed)
            .build();
            
            result.add(resultDto);
        }
        return result;
    }
    
    public double getEmployeedRatePct(int courseSeq) {
        List<EmploymentInfoResponse> result = new ArrayList<>();
        result = getEmploymentInfoByCourseSeq(courseSeq, 0, 1000);
        int numEmployeed = 0;
        int numTotal = result.size();
        for (EmploymentInfoResponse info : result) {
            if(info.isEmployeed())
                numEmployeed++;
        }
        
        return 100 * (double)numEmployeed/numTotal;
    }


}
