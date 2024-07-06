package com.example.hospital.dto.form.doctor;

import com.example.hospital.type.TestType;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TestOrderForm {
    private Long registId;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate orderStartDate;

    private TestType testType;
}
