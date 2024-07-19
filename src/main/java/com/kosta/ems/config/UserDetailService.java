package com.kosta.ems.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.kosta.ems.manager.ManagerMapper;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
@Slf4j
public class UserDetailService implements UserDetailsService {
    private final ManagerMapper managerMapper;

    @Override
    public UserDetails loadUserByUsername(String employeeNumber) throws UsernameNotFoundException {

        return managerMapper.findByEmployeeNumber(employeeNumber);
    }
}
