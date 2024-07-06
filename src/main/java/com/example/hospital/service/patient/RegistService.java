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
import com.example.hospital.security.SecurityManager;
import com.example.hospital.type.RegistType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RegistService {
    private final SecurityManager securityManager;
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
    public RegistResponse register(String token, RegistForm form) {
        Patient patient = patientRepository.findByLoginId(securityManager.parseToken(token).getSubject()).get();

        Doctor doctor = doctorRepository.findById(form.getDoctorId())
                .orElseThrow(() -> new CustomException(ErrorCode.DOCTOR_NOT_FOUND));

        if (registRepository.existsByPatient_IdAndRegistType(patient.getId(), RegistType.REGISTERED) ||
                registRepository.existsByPatient_IdAndRegistType(patient.getId(), RegistType.WAIT_FOR_PAY)) {
            throw new CustomException(ErrorCode.REGIST_EXIST);
        }

        //접수건은 환자당 여러개일수 있다(입,퇴원을 반복한 경우)
        //접수건에는 담당의사, 환자, 입원료, 접수상태, 접수일(입원일), 퇴원일 등이 포함됨
        Regist regist = Regist.builder()
                .doctor(doctor)
                .patient(patient)
                .registType(RegistType.REGISTERED)
                .build();

        return RegistResponse.fromEntity(registRepository.save(regist));
    }

    @Transactional
    public RegistResponse pay(String token) {
        Patient patient = patientRepository.findByLoginId(securityManager.parseToken(token).getSubject()).get();

        //본인 아이디로 등록된 정산 대기중인 접수건을 찾기
        Regist regist = registRepository.findByPatient_IdAndRegistType(patient.getId(), RegistType.WAIT_FOR_PAY)
                .orElseThrow(() -> new CustomException(ErrorCode.REGIST_NOT_FOUND));

        //입원료를 정산한 것으로 취급. 접수 상태를 Discharge(퇴원) 으로 바꾸고 퇴원 날짜를 세팅하기
        regist.setRegistType(RegistType.DISCHARGE);
        regist.setDischargeDate(LocalDateTime.now());
        return RegistResponse.fromEntity(registRepository.save(regist));
    }
}
