package com.kosta.ems.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kosta.ems.config.jwt.StudentDetailService;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class WebSecurityConfig {
    private int count;
    private final UserDetailService userService;
    private final StudentDetailService studentService;

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
//        http.authorizeHttpRequests(
//                authorize -> authorize.requestMatchers("/ems/login").permitAll().anyRequest().authenticated());
//
//        http.formLogin(formLogin -> formLogin.loginPage("/ems/login").defaultSuccessUrl("/ems/courses", true)
//                .failureUrl("/ems/login").usernameParameter("employeeNumber"));
//
//        http.logout(logout -> logout.
//                logoutUrl("/manager/logout")
//                .logoutSuccessUrl("/ems/login").invalidateHttpSession(true)
//                .deleteCookies("JSESSIONID"));
//
//        http.csrf(csrf -> csrf.disable());

        http.csrf(csrf -> csrf.disable());

        http.authorizeHttpRequests(authorize -> {
            authorize.requestMatchers("/ems/login", "/ems/join").permitAll()
                    .requestMatchers("/manager/**").authenticated()
                    .requestMatchers("/api/**").authenticated()
                    .anyRequest().authenticated();
        });

        http.formLogin(formLogin -> formLogin
                        .loginPage("/ems/login")
                        .defaultSuccessUrl("/ems/courses", true)
                        .failureUrl("/ems/login")
                        .usernameParameter("employeeNumber"))
                .logout(logout -> logout
                        .logoutUrl("/manager/logout").logoutSuccessUrl("/ems/login")
                        .invalidateHttpSession(true).deleteCookies("JSESSIONID"))

                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED));

        // JWT 기반 인증 설정
//                      .logoutSuccessUrl("/ems/login")  http.securityMatcher("/api/**")
//                      .invalidateHttpSession(true)          .authorizeHttpRequests(authorize -> authorize
//                      .deleteCookies("JSESSIONID"))                  .requestMatchers(HttpMethod.GET, "/api/**").permitAll()
//                  .sessionManagement(session -> session                  .requestMatchers(HttpMethod.POST, "/api/login").permitAll()
//                      .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED))                  .requestMatchers("/api/**").authenticated()
//                        .anyRequest().authenticated() // 여기에 위치해야 함
//                )
//                .csrf(csrf -> csrf.disable())
//                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class);


        return http.build();
    }

    // 4
    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http, PlainEncoder passwordEncoder,
                                                       UserDetailService userDetailService) throws Exception {

        AuthenticationManagerBuilder authBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);

        // Manager AuthenticationProvider
        DaoAuthenticationProvider managerAuthProvider = new DaoAuthenticationProvider();
        managerAuthProvider.setUserDetailsService(userService);
        managerAuthProvider.setPasswordEncoder(passwordEncoder);
        authBuilder.authenticationProvider(managerAuthProvider);

        // Student AuthenticationProvider
        DaoAuthenticationProvider studentAuthProvider = new DaoAuthenticationProvider();
        studentAuthProvider.setUserDetailsService(studentService);
        studentAuthProvider.setPasswordEncoder(passwordEncoder);
        authBuilder.authenticationProvider(studentAuthProvider);

        return authBuilder.build();
//        return http.getSharedObject(AuthenticationManagerBuilder.class).userDetailsService(userService)
//                .passwordEncoder(passwordEncoder).and().build();
    }

    // 1비밀번호 암호화를 위해

    @Bean
    public PlainEncoder plainEncoder() {
        return new PlainEncoder();
    }


}
