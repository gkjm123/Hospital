package com.example.hospital.config;

import com.example.hospital.security.DoctorDetailService;
import com.example.hospital.security.JwtFilter;
import com.example.hospital.security.PatientDetailService;
import com.example.hospital.security.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true)
public class SecurityConfig {

  private final JwtProvider jwtProvider;
  private final DoctorDetailService doctorDetailService;
  private final PatientDetailService patientDetailService;

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
        .csrf(AbstractHttpConfigurer::disable)
        .formLogin(AbstractHttpConfigurer::disable)
        .authorizeHttpRequests(authorizeRequest ->
            authorizeRequest
                .requestMatchers(AntPathRequestMatcher.antMatcher("sign/**")).permitAll()
                .requestMatchers("/", "/swagger-ui/**", "/v3/api-docs/**").permitAll()
                .anyRequest().authenticated()
        );

    http //JWT 토큰 확인 필터를 UsernamePasswordAuthenticationFilter 보다 앞에 위치
        .addFilterBefore(new JwtFilter(jwtProvider, doctorDetailService, patientDetailService),
            UsernamePasswordAuthenticationFilter.class);

    http //폼 로그인, csrf disable
        .formLogin(AbstractHttpConfigurer::disable)
        .csrf(AbstractHttpConfigurer::disable);

    return http.build();
  }
}
