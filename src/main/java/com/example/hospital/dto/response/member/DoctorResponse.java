package com.example.hospital.dto.response.member;

import com.example.hospital.entity.member.Doctor;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DoctorResponse {
    private Long id;
    private String name;
    private String phone;
    private String major;
    private LocalDateTime createdAt;

    public static DoctorResponse fromEntity(Doctor doctor) {
        return DoctorResponse.builder()
                .id(doctor.getId())
                .name(doctor.getName())
                .phone(doctor.getPhone())
                .major(doctor.getMajor())
                .createdAt(doctor.getCreatedAt())
                .build();
    }
}
