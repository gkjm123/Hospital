package com.example.hospital.dto.form.doctor;

import com.example.hospital.type.MedicineType;
import com.example.hospital.type.TakeType;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MedicineOrderForm {
    private Long registId;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate orderStartDate;

    private MedicineType medicineType;
    private Long volume;
    private TakeType takeType;
    private Long takeDate;
}
