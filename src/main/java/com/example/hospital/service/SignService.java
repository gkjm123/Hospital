package com.example.hospital.service;

import com.example.hospital.dto.form.member.DoctorForm;
import com.example.hospital.dto.form.member.PatientForm;
import com.example.hospital.dto.form.member.SignInForm;
import com.example.hospital.dto.response.member.DoctorResponse;
import com.example.hospital.dto.response.member.PatientResponse;
import com.example.hospital.entity.member.Doctor;
import com.example.hospital.entity.member.Patient;
import com.example.hospital.exception.CustomException;
import com.example.hospital.exception.ErrorCode;
import com.example.hospital.repository.member.DoctorRepository;
import com.example.hospital.repository.member.PatientRepository;
import com.example.hospital.security.SecurityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SignService {
    private final SecurityManager securityManager;
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;

    @Transactional
    public DoctorResponse doctorSignUp(DoctorForm form) {
        if (doctorRepository.existsByLoginId(form.getLoginId())) {
            throw new CustomException(ErrorCode.ID_EXIST);
        }

        Doctor doctor = Doctor.builder()
                .loginId(form.getLoginId())
                .name(form.getName())
                .phone(form.getPhone())
                .major(form.getMajor())
                .build();

        doctor.setPassword(securityManager.passwordEncode(form.getPassword()));
        doctor.setRole("ROLE_DOCTOR");
        return DoctorResponse.fromEntity(doctorRepository.save(doctor));
    }

    @Transactional
    public String doctorSignIn(SignInForm form) {
        Doctor doctor = doctorRepository.findByLoginId(form.getLoginId())
                        .orElseThrow(() -> new CustomException(ErrorCode.ID_PASSWORD_INVALID));

        if (!securityManager.checkPassword(form.getPassword(), doctor.getPassword())) {
            throw new CustomException(ErrorCode.ID_PASSWORD_INVALID);
        }

        String token = securityManager.createToken(form.getLoginId());
        return "doctor " + token;
    }

    @Transactional
    public PatientResponse patientSignUp(PatientForm form) {
        if (patientRepository.existsByLoginId(form.getLoginId())) {
            throw new CustomException(ErrorCode.ID_EXIST);
        }

        Patient patient = Patient.builder()
                .loginId(form.getLoginId())
                .name(form.getName())
                .sex(form.getSex())
                .age(form.getAge())
                .phone(form.getPhone())
                .address(form.getAddress())
                .build();

        patient.setPassword(securityManager.passwordEncode(form.getPassword()));
        patient.setRole("ROLE_PATIENT");
        return PatientResponse.fromEntity(patientRepository.save(patient));
    }

    @Transactional
    public String patientSignIn(SignInForm form) {
        Patient patient = patientRepository.findByLoginId(form.getLoginId())
                .orElseThrow(() -> new CustomException(ErrorCode.ID_PASSWORD_INVALID));

        if (!securityManager.checkPassword(form.getPassword(), patient.getPassword())) {
            throw new CustomException(ErrorCode.ID_PASSWORD_INVALID);
        }

        String token = securityManager.createToken(form.getLoginId());
        return "patient " + token;
    }
}
