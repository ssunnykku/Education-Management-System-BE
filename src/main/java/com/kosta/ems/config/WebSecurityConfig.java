package com.kosta.ems.config;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;


@RequiredArgsConstructor
@Configuration
public class WebSecurityConfig {
	private int count;
    private final UserDetailService userService;
    @Value("${security.level}")
    private String SECURITY_LEVEL; 
    //2. 리소스 접근 빈 설정
    @Bean
    public WebSecurityCustomizer configure() {
    	if(SECURITY_LEVEL.equals("OFF")) {
    	    return (web) -> web.ignoring()  
                    .requestMatchers("/**");
    	}
        return (web) -> web.ignoring()  
                .requestMatchers("/css/**");
    }
    //3
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeRequests()
                    .requestMatchers("/ems/login").permitAll()  //로그인 없이 접근 가능 페이지
                    .anyRequest().authenticated()
                .and()
                .formLogin()
                    .loginPage("/ems/login")
                    .defaultSuccessUrl("/ems/courses", true)
                    .failureUrl("ems/login")
                    .usernameParameter("employeeNumber")
                .and()
                .logout()
                    .logoutSuccessUrl("/ems/login")
                    .invalidateHttpSession(true)
                .and()
                .csrf().disable()    //로컬에서 확인하기 위해 비 활성화
                .build();
    }
    //4
    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http, PlainEncoder passwordEncoder, UserDetailService userDetailService) throws Exception {
    	return http.getSharedObject(AuthenticationManagerBuilder.class)
                .userDetailsService(userService)
                .passwordEncoder(passwordEncoder)
                .and()
                .build();
    }

    //1비밀번호 암호화를 위해
    
    @Bean
    public PlainEncoder plainEncoder() {
        return new PlainEncoder();
    }
}
