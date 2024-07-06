package com.example.hospital.dto.form.doctor;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DiagnosisForm {
    private Long registId;
    private String name;
}
