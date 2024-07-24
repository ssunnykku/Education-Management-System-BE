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
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
@Slf4j
public class WebSecurityConfig {

    private final UserDetailService userService;
    private final StudentDetailService studentService;
    private final JwtTokenProvider jwtTokenProvider;

    @Value("${security.level}")
    private String SECURITY_LEVEL;

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        if (SECURITY_LEVEL.equals("OFF")) {
            return (web) -> web.ignoring().requestMatchers("/**");
        }
        return (web) -> web.ignoring().requestMatchers("/css/**");
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .httpBasic(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/api/students/login","/ems/login").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/**").hasRole("STUDENT")
                        .requestMatchers("/ems/**")
                        .hasRole("MANAGER")
                        .anyRequest().authenticated()
                )
                .formLogin(formLogin -> formLogin
                        .loginPage("/ems/login")
                        .defaultSuccessUrl("/ems", true)
                        .failureUrl("/ems/login")
                        .usernameParameter("employeeNumber")
                )
                .logout(logout -> logout
                        .logoutUrl("/manager/logout")
                        .logoutSuccessUrl("/ems/login")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                );

        http.addFilterBefore(new TokenAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

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


//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }

    @Bean
    public PlainEncoder PlainEncoder() {
        return new PlainEncoder();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:5173"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE"));
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
