package com.example.hospital.dto.form.doctor;

import com.example.hospital.type.Medicine;
import com.example.hospital.type.TakePerDay;
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

  private Medicine medicine;
  private Long volume;
  private TakePerDay takePerDay;
  private Long takeDate;
}
