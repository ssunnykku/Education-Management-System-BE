package com.kosta.ems.manager;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

public interface ManagerService {
	Map<String, String> login(String employeeNumber, String password);
}
