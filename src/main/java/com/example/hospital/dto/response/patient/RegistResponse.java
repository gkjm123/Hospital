package com.example.hospital.dto.response.patient;

import com.example.hospital.entity.regist.Regist;
import com.example.hospital.type.RegistType;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegistResponse {
    private Long id;
    private Long doctorId;
    private Long patientId;
    private String patientName;
    private Long cost;
    private String diagnosis;
    private RegistType registType;
    private LocalDateTime registrationDate;
    private LocalDateTime dischargeDate;

    public static RegistResponse fromEntity(Regist regist) {
        return RegistResponse.builder()
                .id(regist.getId())
                .doctorId(regist.getDoctor().getId())
                .patientId(regist.getPatient().getId())
                .patientName(regist.getPatient().getName())
                .cost(regist.getCost())
                .diagnosis(regist.getDiagnosis())
                .registType(regist.getRegistType())
                .registrationDate(regist.getRegistrationDate())
                .dischargeDate(regist.getDischargeDate())
                .build();
    }
}
