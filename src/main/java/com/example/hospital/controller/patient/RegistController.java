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
@RequestMapping("/patient/reception")
@RequiredArgsConstructor
public class RegistController {

  private final RegistService registService;

  //접수 가능한 의사 목록
  @GetMapping("/doctors")
  public List<DoctorResponse> getDoctors() {
    return registService.getDoctors();
  }

  //입원 접수
  @PreAuthorize("hasRole('PATIENT')")
  @PostMapping("/admission")
  public RegistResponse register(@RequestBody RegistForm form) {

    return registService.register(form);
  }

  //의사가 퇴원 처방하면 총 입원료가 산출되며 퇴원 대기중이 된다. 그 상태에서 환자가 정산시 퇴원 완료
  @PreAuthorize("hasRole('PATIENT')")
  @PutMapping("/payment")
  public RegistResponse pay() {
    return registService.pay();
  }
}
