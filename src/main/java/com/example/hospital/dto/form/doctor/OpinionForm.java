package com.example.hospital.dto.form.doctor;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OpinionForm {
    private Long registId;
    private String opinion;
}
