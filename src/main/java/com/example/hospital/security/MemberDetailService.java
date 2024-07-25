package com.example.hospital.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface MemberDetailService extends UserDetailsService {

  Authentication getAuthentication(String token);

}
