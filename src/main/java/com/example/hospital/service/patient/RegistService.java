package com.example.hospital.service.patient;

import com.example.hospital.dto.form.patient.RegistForm;
import com.example.hospital.dto.response.member.DoctorResponse;
import com.example.hospital.dto.response.patient.RegistResponse;
import com.example.hospital.entity.member.Doctor;
import com.example.hospital.entity.member.Patient;
import com.example.hospital.entity.regist.Regist;
import com.example.hospital.exception.CustomException;
import com.example.hospital.exception.ErrorCode;
import com.example.hospital.repository.member.DoctorRepository;
import com.example.hospital.repository.member.PatientRepository;
import com.example.hospital.repository.regist.RegistRepository;
import com.example.hospital.security.JwtProvider;
import com.example.hospital.type.RegisterStatus;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RegistService {

  private final JwtProvider jwtProvider;
  private final PatientRepository patientRepository;
  private final DoctorRepository doctorRepository;
  private final RegistRepository registRepository;

  @Transactional
  public List<DoctorResponse> getDoctors() {

    List<Doctor> doctors = doctorRepository.findAll();

    if (doctors.isEmpty()) {
      throw new CustomException(ErrorCode.LIST_EMPTY);
    }

    return doctors.stream().map(DoctorResponse::fromEntity).toList();
  }

  @Transactional
  public RegistResponse register(RegistForm form) {

    Patient patient = getPatient();

    Doctor doctor = doctorRepository.findById(form.getDoctorId())
        .orElseThrow(() -> new CustomException(ErrorCode.DOCTOR_NOT_FOUND));

    if (registRepository.existsByPatient_IdAndRegistType(patient.getId(), RegisterStatus.REGISTERED) ||
        registRepository.existsByPatient_IdAndRegistType(patient.getId(), RegisterStatus.WAIT_FOR_PAY)
    ) {
      throw new CustomException(ErrorCode.REGIST_EXIST);
    }

    Regist regist = Regist.builder()
        .doctor(doctor)
        .patient(patient)
        .registerStatus(RegisterStatus.REGISTERED)
        .build();

    return RegistResponse.fromEntity(registRepository.save(regist));
  }

  @Transactional
  public RegistResponse pay() {
    Patient patient = getPatient();

    Regist regist = registRepository
        .findByPatient_IdAndRegistType(patient.getId(), RegisterStatus.WAIT_FOR_PAY)
        .orElseThrow(() -> new CustomException(ErrorCode.REGIST_NOT_FOUND));

    regist.setRegisterStatus(RegisterStatus.DISCHARGE);
    regist.setDischargeDate(LocalDateTime.now());

    return RegistResponse.fromEntity(registRepository.save(regist));
  }

  private Patient getPatient() {
    return (Patient) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
  }
}
