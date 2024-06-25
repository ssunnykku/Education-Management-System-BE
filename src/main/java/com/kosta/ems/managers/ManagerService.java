package com.kosta.ems.managers;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface ManagerService {
	Map<String, String> login(String employeeNumber, String password);
}
