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

    //접수 가능한 의사 목록
    @GetMapping("/get-doctors")
    public List<DoctorResponse> getDoctors() {
        return registService.getDoctors();
    }

    //원하는 의사 아이디를 폼에 넣어 접수하기(수락 과정 없이 자동으로 등록됨)
    @PreAuthorize("hasRole('PATIENT')")
    @PostMapping("/register")
    public RegistResponse register(@RequestHeader(name = "TOKEN") String token, @RequestBody RegistForm form) {
        return registService.register(token, form);
    }

    //의사가 퇴원 처방하면 총 입원료가 산출되며 퇴원 대기중이 된다. 그 상태에서 환자가 정산시 퇴원 처리됨.
    @PreAuthorize("hasRole('PATIENT')")
    @PutMapping("/pay")
    public RegistResponse pay(@RequestHeader(name = "TOKEN") String token) {
        return registService.pay(token);
    }
}
