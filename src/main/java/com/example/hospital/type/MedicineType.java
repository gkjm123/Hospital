package com.example.hospital.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MedicineType {
  PAIN_MED("진통제", "알약", 1000L),
  FEVER_MED("해열제", "알약", 500L),
  ANTIBIOTIC_MED("항생제", "주사약", 2000L);

  private final String description;
  private final String type;
  private final Long cost;
}