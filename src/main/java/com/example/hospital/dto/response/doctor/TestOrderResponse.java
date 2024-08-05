package com.example.hospital.dto.response.doctor;

import com.example.hospital.entity.order.TestOrder;
import com.example.hospital.type.OrderStatus;
import com.example.hospital.type.Order;
import com.example.hospital.type.Test;
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
  private Order order;
  private OrderStatus orderStatus;
  private Long cost;
  private LocalDateTime orderCreateTime;
  private LocalDate orderStartDate;
  private Test test;

  public static TestOrderResponse fromEntity(TestOrder testOrder) {
    return TestOrderResponse.builder()
        .id(testOrder.getId())
        .registId(testOrder.getRegist().getId())
        .order(testOrder.getOrder())
        .orderStatus(testOrder.getOrderStatus())
        .cost(testOrder.getCost())
        .orderCreateTime(testOrder.getOrderCreateTime())
        .orderStartDate(testOrder.getOrderStartTime())
        .test(testOrder.getTest())
        .build();
  }
}
