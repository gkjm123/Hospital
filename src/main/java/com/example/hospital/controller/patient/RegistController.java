package com.example.hospital.controller.patient;

import com.example.hospital.dto.form.patient.RegistForm;
import com.example.hospital.dto.response.member.DoctorResponse;
import com.example.hospital.dto.response.patient.RegistResponse;
import com.example.hospital.service.patient.RegistService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/patient/regist")
@RequiredArgsConstructor
public class RegistController {
    private final RegistService registService;

    @GetMapping("/get-doctors")
    public List<DoctorResponse> getDoctors() {
        return registService.getDoctors();
    }

    @PreAuthorize("hasRole('PATIENT')")
    @PostMapping("/register")
    public RegistResponse register(@RequestHeader(name = "TOKEN") String token, @RequestBody RegistForm form) {
        return registService.register(token, form);
    }

    @PreAuthorize("hasRole('PATIENT')")
    @PutMapping("/pay")
    public RegistResponse pay(@RequestHeader(name = "TOKEN") String token) {
        return registService.pay(token);
    }
}
