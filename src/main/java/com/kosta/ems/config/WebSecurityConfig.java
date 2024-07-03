package com.kosta.ems.config;

import lombok.RequiredArgsConstructor;
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
    //2. 리소스 접근 빈 설정
    @Bean
    public WebSecurityCustomizer configure() {
    	System.out.println(++count+ " WebSecurityConfig's configure()");
        return (web) -> web.ignoring()  
                .requestMatchers("/static/**");
    }
    //3
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    	System.out.println(++count+ " WebSecurityConfig's filterChain()");
        return http
                .authorizeRequests()
                    .requestMatchers("/ems/login").permitAll()  //로그인 없이 접근 가능 페이지
                    .anyRequest().authenticated()
                .and()
                .formLogin()
                    .loginPage("/ems/login")            
                    .defaultSuccessUrl("/ems/courses")
                    .usernameParameter("employeeNumber")
                .and()
                .logout()
                    .logoutSuccessUrl("/login")
                    .invalidateHttpSession(true)
                .and()
                .csrf().disable()    //로컬에서 확인하기 위해 비 활성화
                .build();
    }
    //4
    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http, PlainEncoder passwordEncoder, UserDetailService userDetailService) throws Exception {
        System.out.println(++count+ " WebSecurityConfig's authenticationManager()");
    	return http.getSharedObject(AuthenticationManagerBuilder.class)
                .userDetailsService(userService)
                .passwordEncoder(passwordEncoder)
                .and()
                .build();
    }

    //1비밀번호 암호화를 위해
    
    @Bean
    public PlainEncoder plainEncoder() {
    	 System.out.println(++count+ " WebSecurityConfig's PlainEncoder()");
        return new PlainEncoder();
    }
}
