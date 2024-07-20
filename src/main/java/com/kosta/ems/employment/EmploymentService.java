package com.kosta.ems.employment;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.kosta.ems.course.CourseDTO;
import com.kosta.ems.course.CourseMapper;
import com.kosta.ems.employment.dto.EditEmployeedStatusRequest;
import com.kosta.ems.student.dto.GetStudentInfoByScqDTO;
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

    public List<EmploymentInfoResponse> getEmploymentInfoByCourseNumber(int courseNumber) {
        CourseDTO course = courseMapper.getCourseByCourseNumber(courseNumber);
        return getEmploymentInfoByCourseSeq(course.getCourseSeq());
    }

    //TODO: pageable 기능 구현하기
    public List<EmploymentInfoResponse> getEmploymentInfoByCourseSeq(int courseSeq) {
        List<EmploymentInfoResponse> result = new ArrayList<>();
        CourseDTO course = courseMapper.getCourse(courseSeq);
        //과정에 다니는 학생 seq 목록을 받아오고 
        List<StudentCourseDTO> sCList = sCRepo.findByCourseSeq(courseSeq);
        //각 학생의 employment 정보와 학생 기본정보를 result에 넣는다.
        for (StudentCourseDTO sCDto : sCList) {
            EmploymentInfoResponse resultDto;
            //학생seq로 학생정보를 받아오고
            GetStudentInfoByScqDTO studentDto = studentMapper.selectStudentInfoByScq(sCDto.getSeq());
            //또한 고용정보를 받아온다.
            EmploymentDTO employmentDto = repo.findBysCSeq(sCDto.getSeq()).orElse(EmploymentDTO.builder().company("").build());

            resultDto = EmploymentInfoResponse.builder()
                    .employmentSeq(employmentDto.getSeq())
                    .sCSeq(sCDto.getSeq())
                    .hrdNetId(studentDto.getHrdNetId())
                    .courseNumber(course.getCourseNumber())
                    .name(studentDto.getName())
                    .phoneNumber(studentDto.getPhoneNumber())
                    .email(studentDto.getEmail())
                    .courseEndDate(course.getCourseEndDate())
                    .company(employmentDto.getCompany())
                    .isEmployeed(employmentDto.getCompany().equals("") ? false : true)
                    .build();

            result.add(resultDto);
        }
        return result;
    }

    public double getEmployeedRatePct(int courseNumber) {
        List<EmploymentInfoResponse> result = new ArrayList<>();
        int courseSeq = courseMapper.getCourseByCourseNumber(courseNumber).getCourseSeq();
        result = getEmploymentInfoByCourseSeq(courseSeq);
        int numEmployeed = 0;
        int numTotal = result.size();
        for (EmploymentInfoResponse info : result) {
            if (info.isEmployeed())
                numEmployeed++;
        }

        return 100 * (double) numEmployeed / numTotal;
    }

    public boolean editEmployeedStatus(EditEmployeedStatusRequest request, String managerId) {
        EmploymentDTO dto = repo.findBysCSeq(request.getSCSeq()).orElseGet(() -> EmploymentDTO.
                builder()
                .sCSeq(request.getSCSeq())
                .build());
        dto.setCompany(request.getCompany());
        dto.setManagerId(managerId);
        if (repo.save(dto) == null) {
            return false;
        }
        return true;
    }

    public int countEmployeedByCourseNumber(int courseNumber) {
        List<EmploymentInfoResponse> result = new ArrayList<>();
        int courseSeq = courseMapper.getCourseByCourseNumber(courseNumber).getCourseSeq();
        result = getEmploymentInfoByCourseSeq(courseSeq);
        int numEmployeed = 0;
        int numTotal = result.size();
        for (EmploymentInfoResponse info : result) {
            if (info.isEmployeed())
                numEmployeed++;
        }
        return numEmployeed;
    }

}
