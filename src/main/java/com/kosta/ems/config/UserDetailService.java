package com.kosta.ems.config;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.kosta.ems.manager.ManagerMapper;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UserDetailService implements UserDetailsService{
	private final ManagerMapper managerMapper;

	@Override
	public UserDetails loadUserByUsername(String employeeNumber) throws UsernameNotFoundException {
		System.out.println("userDetailService's loadUserByUserName");
		return managerMapper.findByEmployeeNumber(employeeNumber);
	}
}
