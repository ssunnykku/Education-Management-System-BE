package com.kosta.ems.course.dto;

import java.time.LocalDate;

import com.kosta.ems.course.CourseDTO;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Getter;

@Data
@Getter
public class AddCourseRequest {
    @Min(value=1, message="기수를 입력해주세요.")
    private int courseNumber;
    
    @NotEmpty(message="과정명을 입력해주세요.")
    @Size(max=100,message="과정명은 100자를 초과할 수 없습니다.")
    private String courseName;
    
    @NotNull(message="시작 날짜를 입력해주세요.")
    private LocalDate courseStartDate;
    
    @FutureOrPresent(message="이미 지난 날짜입니다.")
    private LocalDate courseEndDate;
    
    @NotEmpty(message="주제를 작성해주세요.")
    @Size(max=100,message="주제명은 100자를 초과할 수 없습니다.")
    private String subject;
    
    @NotEmpty(message="과정 유형을 작성해주세요.")
    @Size(max=100,message="과정 구분은 100자를 초과할 수 없습니다.")
    private String courseType;
    
    @Min(value=10,message="10일보다 짧은 교육과정은 없습니다.")
    @Max(value=300,message="300일 이상 교육과정을 등록할 수는 없습니다.")
    private int totalTrainingDays;
    
    @Min(value=1,message="최소한 하루 1시간 이상 공부해야 합니다.")
    @Max(value=12,message="하루 12시간 이상 공부할 수는 없습니다.")
    private int trainingHoursOfDate;
    
    @NotEmpty(message="교수 이름을 입력해주세요.")
    @Size(max=100,message="강사명은 100자를 초과할 수 없습니다.")
    private String professorName;
    @Min(value=1, message="최소 1명 이상의 학생이 등록할 수 있습니다.")
    @Max(value =50, message="1개의 과정에는 최대 50명까지만 등록할 수 있습니다.")
    private int maxStudents;
    
    
    public CourseDTO toCourseDTO() {
        CourseDTO dto;
        dto = CourseDTO.builder()
                .courseNumber(courseNumber)
                .courseName(courseName)
                .subject(subject)
                .courseType(courseType)
                .courseStartDate(courseStartDate)
                .courseEndDate(courseEndDate)
                .professorName(professorName)
                .totalTrainingDays(totalTrainingDays)
                .trainingHoursOfDate(trainingHoursOfDate)
                .maxStudents(maxStudents)
                .build();
        return dto;
    }
}
