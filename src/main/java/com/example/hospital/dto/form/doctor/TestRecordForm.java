package com.example.hospital.dto.form.doctor;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TestRecordForm {

  private Long testOrderId;
  private String result;
}
