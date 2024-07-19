package com.kosta.ems.studentLogin;

import com.kosta.ems.student.StudentCourseInfoDTO;
import com.kosta.ems.student.StudentDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserMapper {
    StudentDTO getLoginInfo(String hrdNetId);

    String studentLogin(String hrdNetId, String password);

    void setRefreshToken(String hrdNetId, String refreshToken);

    String getRefreshToken(String hrdNetId);

    String findByToken(String refreshToken);

    void removeToken(String hrdNetId);

    List<StudentCourseInfoDTO> selectStudentListBycourseSeq(int courseSeq);


}