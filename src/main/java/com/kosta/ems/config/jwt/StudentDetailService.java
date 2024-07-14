package com.kosta.ems.config.jwt;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import com.kosta.ems.student.StudentMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class StudentDetailService implements UserDetailsService {
    private final StudentMapper studentMapper;

    @Override
    public UserDetails loadUserByUsername(String hrdNetId) throws UsernameNotFoundException {
        return studentMapper.getLoginInfo(hrdNetId);
    }
}
