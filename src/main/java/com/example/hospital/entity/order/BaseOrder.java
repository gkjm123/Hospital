package com.example.hospital.entity.order;

import com.example.hospital.entity.regist.Regist;
import com.example.hospital.type.OrderStatus;
import com.example.hospital.type.Order;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class BaseOrder {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  //접수건
  @ManyToOne(fetch = FetchType.LAZY)
  private Regist regist;

  //처방 종류(약, 검사)
  @Enumerated(EnumType.STRING)
  private Order order;

  //처방의 진행상태
  @Enumerated(EnumType.STRING)
  private OrderStatus orderStatus;

  //비용
  private Long cost;

  //처방 생성일
  @CreationTimestamp
  private LocalDateTime orderCreateTime;

  //처방 수행 시작일(약복용 시작일, 검사 시행일)
  private LocalDate orderStartTime;
}
