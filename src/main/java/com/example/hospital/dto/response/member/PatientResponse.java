package com.example.hospital.dto.response.member;

import com.example.hospital.entity.member.Patient;
import com.example.hospital.type.Gender;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PatientResponse {

  private Long id;
  private String name;
  private Gender sex;
  private Long age;
  private String phone;
  private String address;
  private LocalDateTime createdAt;

  public static PatientResponse fromEntity(Patient patient) {
    return PatientResponse.builder()
        .id(patient.getId())
        .name(patient.getName())
        .sex(patient.getSex())
        .age(patient.getAge())
        .phone(patient.getPhone())
        .address(patient.getAddress())
        .createdAt(patient.getCreatedAt())
        .build();
  }
}
