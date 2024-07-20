package com.kosta.ems.manager;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ManagerServiceImpl implements ManagerService {
    private final ManagerMapper managerMapper;

    @Override
    public Map<String, String> login(String employeeNumber, String password) {
        return managerMapper.login(employeeNumber, password);
    }

    @Override
    public ManagerDTO findByEmployeeNumber(String employeeNumber) {
        return managerMapper.findByEmployeeNumber(employeeNumber);
    }

    @Override
    public ManagerDTO fintByManagerId(String managerId) {
        return managerMapper.findByManagerId(managerId);
    }

}
