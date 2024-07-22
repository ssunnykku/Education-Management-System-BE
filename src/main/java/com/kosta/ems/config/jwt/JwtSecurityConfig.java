package com.kosta.ems.config.jwt;

import com.kosta.ems.config.PlainEncoder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@Slf4j
public class JwtSecurityConfig {
    private final JwtTokenProvider jwtTokenProvider;
    private final StudentDetailService studentService;


    @Bean
    public SecurityFilterChain jwtFilterChain(HttpSecurity http) throws Exception {

        http.httpBasic(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .authorizeHttpRequests(authorize -> {
                    authorize.requestMatchers("/api/students/login").permitAll()
                            .requestMatchers(HttpMethod.POST, "/api/**").authenticated()
                            .requestMatchers("/api/**").authenticated()
                            .anyRequest().authenticated();
                })
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(new TokenAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class);
//                .authenticationManager(jwtAuthenticationManager);

        return http.build();

    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:5173")); // 허용할 출처 설정
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE")); // 허용할 HTTP 메서드 설정
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type")); // 허용할 헤더 설정
        configuration.setAllowCredentials(true); // 자격 증명 허용 여부 설정

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

//    @Bean
//    public AuthenticationManager jwtAuthenticationManager(HttpSecurity http, PlainEncoder passwordEncoder) throws Exception {
//
//        AuthenticationManagerBuilder authBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
//
//        log.info("{}", http);
//        log.info("authBuilder {} ", authBuilder);
//
//
//        // Student AuthenticationProvider
//        DaoAuthenticationProvider studentAuthProvider = new DaoAuthenticationProvider();
//        studentAuthProvider.setUserDetailsService(studentService);
//        studentAuthProvider.setPasswordEncoder(passwordEncoder);
//        authBuilder.authenticationProvider(studentAuthProvider);
//
//        return authBuilder.build();
//
//    }
//
////    @Bean
////    public PlainEncoder jwtPlainEncoder() {
////        return new PlainEncoder();
////    }
//
//    @Bean
//    public PasswordEncoder jwtPlainEncoder() {
//        return new BCryptPasswordEncoder();
//    }

}
