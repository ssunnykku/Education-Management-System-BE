package com.kosta.ems.config.jwt;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import com.kosta.ems.student.StudentMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Slf4j
public class StudentDetailService implements UserDetailsService {
    private final StudentMapper studentMapper;

    @Override
    public UserDetails loadUserByUsername(String hrdNetId) throws UsernameNotFoundException {
        log.info(hrdNetId);
        log.info("사용자 정보 {} ", studentMapper.getLoginInfo(hrdNetId));
        return studentMapper.getLoginInfo(hrdNetId);
    }
}
