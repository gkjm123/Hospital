package com.example.hospital.controller;

import com.example.hospital.dto.form.member.DoctorForm;
import com.example.hospital.dto.form.member.PatientForm;
import com.example.hospital.dto.form.member.SignInForm;
import com.example.hospital.dto.response.member.DoctorResponse;
import com.example.hospital.dto.response.member.PatientResponse;
import com.example.hospital.service.SignService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/sign")
@RequiredArgsConstructor
public class SignController {

  private final SignService signService;

  @PostMapping("/doctor/join")
  public DoctorResponse doctorSignUp(@RequestBody DoctorForm form) {
    return signService.doctorSignUp(form);
  }

  @GetMapping("/doctor/login")
  public String doctorSignIn(@RequestBody SignInForm form) {
    return signService.doctorSignIn(form);
  }

  @PostMapping("/patient/join")
  public PatientResponse patientSignUp(@RequestBody PatientForm form) {
    return signService.patientSignUp(form);
  }

  @GetMapping("/patient/login")
  public String patientSignIn(@RequestBody SignInForm form) {
    return signService.patientSignIn(form);
  }
}
