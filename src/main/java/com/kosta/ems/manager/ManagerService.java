package com.kosta.ems.manager;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

public interface ManagerService {
    Map<String, String> login(String employeeNumber, String password);

    ManagerDTO findByEmployeeNumber(String employeeNumber);

    ManagerDTO fintByManagerId(String managerId);

    // 프로필 이미지 파일 업로드
    /*void updateProfileImage(String managerId, ManagerDTO dto);*/
    void updateProfileImage(String managerId, String imgURL);
}
