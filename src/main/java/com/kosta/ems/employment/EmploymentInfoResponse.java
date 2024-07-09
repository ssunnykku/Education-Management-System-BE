package com.kosta.ems.employment;

import java.time.LocalDate;

import com.kosta.ems.course.CourseDTO;
import com.kosta.ems.student.GetStudentInfoByScqDTO;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Builder
@Setter
@ToString
public class EmploymentInfoResponse {
    private int sCSeq;
    private String hrdNetId;
    private int courseNumber;
    private String name;
    private String phoneNumber;
    private String email;
    private LocalDate courseEndDate;
    private String company;
    private boolean isEmployeed;
    

}
