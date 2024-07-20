package com.kosta.ems.employment;

import com.kosta.ems.course.CourseDTO;
import com.kosta.ems.student.dto.GetStudentInfoByScqDTO;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Builder
@Setter
@ToString

public class EmploymentInfoDTO {
    private GetStudentInfoByScqDTO student;
    private EmploymentDTO employmentInfo;
    private CourseDTO course;
    private boolean isEmployeed;

}
