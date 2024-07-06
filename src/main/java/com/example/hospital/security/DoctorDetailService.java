package com.example.hospital.security;

import com.example.hospital.exception.CustomException;
import com.example.hospital.exception.ErrorCode;
import com.example.hospital.repository.member.DoctorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DoctorDetailService implements UserDetailsService {
    private final DoctorRepository doctorRepository;
    private final SecurityManager securityManager;

    @Override
    public UserDetails loadUserByUsername(String token) throws UsernameNotFoundException {
        String loginId = securityManager.parseToken(token).getSubject();
        return doctorRepository.findByLoginId(loginId)
                .orElseThrow(() -> new CustomException(ErrorCode.DOCTOR_NOT_FOUND));
    }

    public Authentication getAuthentication(String token) {
        UserDetails userDetails = loadUserByUsername(token);
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }
}