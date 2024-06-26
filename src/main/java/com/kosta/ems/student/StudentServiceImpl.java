package com.kosta.ems.student;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
// import org.springframework.data.domain.Page;

import com.kosta.ems.attendance.StudentAttendanceListDTO;
import com.kosta.ems.attendance.UpdateStudentAttendanceStatusDTO;

import java.time.LocalDate;
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
    
    // 수강생 정보 검색 결과 데이터 개수 (for 페이지네이션)
    @Override
    public int getStudentsByNameOrCourseNumberAmount(String name, int courseNumber) {
    	return studentMapper.findByStudentNumberOrCourseNumberAll(name, courseNumber);
    }
    
    // 수강생 정보 검색 결과 데이터 불러오기
    @Override
    // public Map<String, Collection> getStudentsByNameOrCourseNumber(String name, int courseNumber) {
    public List<StudentBasicInfoDTO> getStudentsByNameOrCourseNumberList(String name, int courseNumber, int page, int size) {
		/*
		 * Map<String, Collection> result = new HashMap<String, Collection>();
		 * result.put("data", studentMapper.findByStudentNameOrCourseNumber(name,
		 * courseNumber)); return result;
		 */
    	// return studentMapper.findByStudentNameOrCourseNumber(name, courseNumber);
    	return studentMapper.findByStudentNameOrCourseNumberList(name, courseNumber, page-1, size);
    }

    // * 수강생 등록
    // ** 입력 id가 등록된 hrdNetId인지 확인
    @Override
    public boolean findByHrdNetId(String hrdNetId) {
        int count = studentMapper.findByHrdNetId(hrdNetId);
        if(count != 0) {
            // 수강생 신규 등록 진행
            return false;
        } else {
            // 수강생 기존 정보 불러오기 + DB엔 수강생_강좌에 새로 강좌 등록하기
            return true;
        }
    }
    
    // * 수강생 등록
    // * 기존 수강생인 경우, 수강생 기본 정보 불러오기
    @Override
    public RegisteredStudentInfoDTO getRegisteredStudentBasicInfo(String hrdNetId) {
    	return studentMapper.selectRegisteredStudentBasicInfo(hrdNetId);
    }
    
    // * 수강생 등록
    // ** 신규 수강생 등록    
    @Override
    public void setStudentWithCourse(String hrdNetId, String name, String birth, String address, String bank, String account, String phoneNumber, String email, String gender, String managerId, String courseNumber) {
    	int year = Integer.parseInt(birth.split("-")[0]);
        int month = Integer.parseInt(birth.split("-")[1]);
        int day = Integer.parseInt(birth.split("-")[2]);
        char g = gender.toCharArray()[0];
    	
        AddStudentBasicInfoDTO dto = AddStudentBasicInfoDTO.builder().hrdNetId(hrdNetId).name(name).birth(LocalDate.of(year, month, day)).address(address).bank(bank).account(account).phoneNumber(phoneNumber).email(email).gender(g).managerId(managerId).courseNumber(Integer.parseInt(courseNumber)).build();
       
        int result1 = studentMapper.addStudentBasicInfo(dto);
        if(result1 == 0) {
        	throw new NoSuchDataException("Fail:: Add new student");
        }
        
        int result2 = studentMapper.addStudentCourseSeqInfo(dto);
        if(result2 == 0) {
        	throw new NoSuchDataException("Fail:: Add student_course");
        }
    }
    
    // ** students 테이블에 수강생 데이터 등록
    @Override
    public void setStudentBasicInfo(String hrdNetId, String name, String birth, String address, String bank, String account, String phoneNumber, String email, String gender, String managerId, String courseNumber) {
        int year = Integer.parseInt(birth.split("-")[0]);
        int month = Integer.parseInt(birth.split("-")[1]);
        int day = Integer.parseInt(birth.split("-")[2]);
        char g = gender.toCharArray()[0];
        AddStudentBasicInfoDTO dto = AddStudentBasicInfoDTO.builder().hrdNetId(hrdNetId).name(name).birth(LocalDate.of(year, month, day)).address(address).bank(bank).account(account).phoneNumber(phoneNumber).email(email).gender(g).managerId(managerId).courseNumber(Integer.parseInt(courseNumber)).build();
        studentMapper.addStudentBasicInfo(dto);
    }
    
    // * 수강생 등록
    // ** students_courses 테이블에 수강생 데이터 등록
    @Override
    public void setStudentCourseSeqInfo(String hrdNetId, String courseNumber) {
        // AddStudentCourseSeqDTO dto = AddStudentCourseSeqDTO.builder().hrdNetId(hrdNetId).courseNumber(Integer.parseInt(courseNumber)).build();
        // studentMapper.addStudentCourseSeqInfo(dto);
    	AddStudentBasicInfoDTO dto = AddStudentBasicInfoDTO.builder().hrdNetId(hrdNetId).courseNumber(Integer.parseInt(courseNumber)).build();
        studentMapper.addStudentCourseSeqInfo(dto);
    }
    
    // 수강생 정보 수정
    @Override
    public void updateSelectedStudentInfo(String name, String address, String bank, String account, String phoneNumber, String email, String studentId) {
    	UpdateSelectedStudentInfoDTO dto = UpdateSelectedStudentInfoDTO.builder().name(name).address(address).bank(bank).account(account).phoneNumber(phoneNumber).email(email).studentId(studentId).build();
    	studentMapper.updateSelectedStudentInfo(dto);
    }
    
    // 수강생 삭제
    @Override
    public void removeSelectedStudent(String studentId) {
    	studentMapper.deleteSelectedStudent(studentId);
    }
    
}