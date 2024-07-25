package com.example.hospital.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TestType {
  BLOOD_TEST(3000L),
  X_RAY(5000L),
  CT(10000L),
  MRI(2000L);

  private final Long cost;
}