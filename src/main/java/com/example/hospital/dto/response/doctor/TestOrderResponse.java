package com.example.hospital.dto.response.doctor;

import com.example.hospital.entity.order.TestOrder;
import com.example.hospital.type.OrderStatusType;
import com.example.hospital.type.OrderType;
import com.example.hospital.type.TestType;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TestOrderResponse {

  private Long id;
  private Long registId;
  private OrderType orderType;
  private OrderStatusType orderStatusType;
  private Long cost;
  private LocalDateTime orderCreateTime;
  private LocalDate orderStartDate;
  private TestType testType;

  public static TestOrderResponse fromEntity(TestOrder testOrder) {
    return TestOrderResponse.builder()
        .id(testOrder.getId())
        .registId(testOrder.getRegist().getId())
        .orderType(testOrder.getOrderType())
        .orderStatusType(testOrder.getOrderStatusType())
        .cost(testOrder.getCost())
        .orderCreateTime(testOrder.getOrderCreateTime())
        .orderStartDate(testOrder.getOrderStartTime())
        .testType(testOrder.getTestType())
        .build();
  }
}
