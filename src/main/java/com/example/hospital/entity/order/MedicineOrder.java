package com.example.hospital.entity.order;

import com.example.hospital.type.MedicineType;
import com.example.hospital.type.TakeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class MedicineOrder extends BaseOrder {

  //약 종류
  @Enumerated(EnumType.STRING)
  private MedicineType medicineType;

  //복용량
  private Long volume;

  //복용법
  @Enumerated(EnumType.STRING)
  private TakeType takeType;

  //복용일수
  private Long takeDate;
}
