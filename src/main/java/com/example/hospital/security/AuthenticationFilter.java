package com.example.hospital.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthenticationFilter extends OncePerRequestFilter {

  private final DoctorDetailService doctorDetailService;
  private final SecurityManager securityManager;
  private final PatientDetailService patientDetailService;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {
    String token = request.getHeader("TOKEN");

    if (token != null && securityManager.parseToken(token) != null) {
      if (token.startsWith("doctor ")) {
        SecurityContextHolder.getContext()
            .setAuthentication(doctorDetailService.getAuthentication(token));
      } else if (token.startsWith("patient ")) {
        SecurityContextHolder.getContext()
            .setAuthentication(patientDetailService.getAuthentication(token));
      }
    }

    filterChain.doFilter(request, response);
  }
}
