package com.kosta.ems.config;

import com.kosta.ems.config.jwt.JwtTokenProvider;
import com.kosta.ems.config.jwt.StudentDetailService;
import com.kosta.ems.config.jwt.TokenAuthenticationFilter;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class WebSecurityConfig {
    private int count;
    private final UserDetailService userService;
    private final StudentDetailService studentService;
    private final JwtTokenProvider jwtTokenProvider;

    @Value("${security.level}")
    private String SECURITY_LEVEL;
    @Value("${jwt.secret}")
    private String SECURITY_KEY;

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

        http.csrf(csrf -> csrf.disable());


        http.httpBasic(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorize -> {
                    authorize.requestMatchers("/ems/login", "/api/students/login", "/api/token").permitAll()
                            .requestMatchers(HttpMethod.POST, "/api/**").authenticated()
                            .requestMatchers("/api/**").authenticated()
                            .anyRequest().authenticated();
                })

                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED))
                .addFilterBefore(new TokenAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class);

        http.formLogin(formLogin -> formLogin
                        .loginPage("/ems/login")
                        .defaultSuccessUrl("/ems/courses", true)
                        .failureUrl("/ems/login")
                        .usernameParameter("employeeNumber"))
                .logout(logout -> logout
                        .logoutUrl("/manager/logout").logoutSuccessUrl("/ems/login")
                        .invalidateHttpSession(true).deleteCookies("JSESSIONID"));

        return http.build();
    }

    // 4
    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http, PlainEncoder passwordEncoder) throws Exception {

        AuthenticationManagerBuilder authBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);

        log.info("{}", http);
        log.info("authBuilder {} ", authBuilder);

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

    }

    // 1비밀번호 암호화를 위해
    @Bean
    public PlainEncoder plainEncoder() {
        return new PlainEncoder();
    }

}
