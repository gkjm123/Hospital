package com.example.hospital.entity.order;

import com.example.hospital.type.Test;
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
public class TestOrder extends BaseOrder {

  //검사 종류
  @Enumerated(EnumType.STRING)
  private Test test;
}