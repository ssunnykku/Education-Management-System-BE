package com.kosta.ems.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.web.SecurityFilterChain;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class WebSecurityConfig {
    private int count;
    private final UserDetailService userService;
    private final ObjectMapper objectMapper;

    @Value("${security.level}")
    private String SECURITY_LEVEL;

    // 2. 리소스 접근 빈 설정
    @Bean
    public WebSecurityCustomizer configure() {
        if (SECURITY_LEVEL.equals("OFF")) {
            return (web) -> web.ignoring().requestMatchers("/**");
        }
        return (web) -> web.ignoring().requestMatchers("/css/**");
    }

    // 3
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(
                authorize -> authorize.requestMatchers("/ems/login").permitAll().anyRequest().authenticated());

        http.formLogin(formLogin -> formLogin.loginPage("/ems/login").defaultSuccessUrl("/ems/courses", true)
                .failureUrl("/ems/login").usernameParameter("employeeNumber"));

        http.logout(logout -> logout.
                logoutUrl("/manager/logout")
                .logoutSuccessUrl("/ems/login").invalidateHttpSession(true)
                .deleteCookies("JSESSIONID"));

        http.csrf(csrf -> csrf.disable());


        return http.build();
    }

    // 4
    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http, PlainEncoder passwordEncoder,
                                                       UserDetailService userDetailService) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class).userDetailsService(userService)
                .passwordEncoder(passwordEncoder).and().build();
    }

    // 1비밀번호 암호화를 위해

    @Bean
    public PlainEncoder plainEncoder() {
        return new PlainEncoder();
    }


}
