package com.kosta.ems.config.jwt;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import com.kosta.ems.studentLogin.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Slf4j
public class StudentDetailService implements UserDetailsService {
    private final UserMapper studentLoginMapper;

    @Override
    public UserDetails loadUserByUsername(String hrdNetId) throws UsernameNotFoundException {
        log.info(hrdNetId);
        log.info("사용자 정보 {} ", studentLoginMapper.getLoginInfo(hrdNetId));
        return studentLoginMapper.getLoginInfo(hrdNetId);
    }
}
