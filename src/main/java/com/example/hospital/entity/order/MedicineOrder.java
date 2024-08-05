package com.example.hospital.entity.order;

import com.example.hospital.type.Medicine;
import com.example.hospital.type.TakePerDay;
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
  private Medicine medicine;

  //복용량
  private Long volume;

  //복용법
  @Enumerated(EnumType.STRING)
  private TakePerDay takePerDay;

  //복용일수
  private Long takeDate;
}
