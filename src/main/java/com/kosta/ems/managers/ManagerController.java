package com.kosta.ems.managers;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/manager")
@RequiredArgsConstructor
@Slf4j
public class ManagerController {
	private final ManagerService service;
	
	//Controller처럼 작동함
	@PostMapping("/login")
	public void login(@RequestBody Map<String, String> loginRequest, HttpServletRequest request, HttpServletResponse response) throws IOException {
		Map<String, String> map = service.login(loginRequest.get("employeeNumber"), loginRequest.get("password"));
		if(Objects.isNull(map)) {
			response.sendRedirect("/ui/login");
			return;
		}
		HttpSession session = request.getSession();
		session.setAttribute("managerId", map.get("managerId"));
		session.setAttribute("academyLocation", map.get("academyLocation"));
		log.info("managerId: " + map.get("managerId").toString());
		log.info("managerId: " + map.get("academyLocation").toString());
		response.sendRedirect("/ui/notifications");
	}
	
	@PostMapping("/logout")
	public void logout(HttpServletRequest request, HttpServletResponse response) throws IOException {
		HttpSession session = request.getSession();
		if(session != null) {
			session.invalidate();
		}
		response.sendRedirect("/ui/login");
	}
}
