package com.example.hospital.dto.response.doctor;

import com.example.hospital.entity.record.Opinion;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OpinionResponse {
    private Long id;
    private Long registId;
    private String opinion;
    private LocalDateTime created;

    public static OpinionResponse fromEntity(Opinion opinion) {
        return OpinionResponse.builder()
                .id(opinion.getId())
                .registId(opinion.getRegist().getId())
                .opinion(opinion.getOpinion())
                .created(opinion.getCreated())
                .build();
    }
}
