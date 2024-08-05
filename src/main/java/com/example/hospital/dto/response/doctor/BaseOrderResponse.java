package com.example.hospital.dto.response.doctor;

import com.example.hospital.entity.order.BaseOrder;
import com.example.hospital.entity.order.MedicineOrder;
import com.example.hospital.entity.order.TestOrder;
import com.example.hospital.type.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BaseOrderResponse {

  private Long id;
  private Long registId;
  private Order order;
  private OrderStatus orderStatus;
  private Long cost;
  private LocalDateTime orderCreateTime;
  private LocalDate orderStartDate;

  private Medicine medicine;
  private Long volume;
  private TakePerDay takePerDay;
  private Long takeDate;

  private Test test;

  public static BaseOrderResponse fromEntity(BaseOrder baseOrder) {
    BaseOrderResponse response = BaseOrderResponse.builder()
        .id(baseOrder.getId())
        .registId(baseOrder.getRegist().getId())
        .order(baseOrder.getOrder())
        .orderStatus(baseOrder.getOrderStatus())
        .cost(baseOrder.getCost())
        .orderCreateTime(baseOrder.getOrderCreateTime())
        .orderStartDate(baseOrder.getOrderStartTime())
        .build();

    if (baseOrder.getOrder().equals(Order.MEDICINE)) {
      MedicineOrder medicineOrder = (MedicineOrder) baseOrder;

      response.setMedicine(medicineOrder.getMedicine());
      response.setVolume(medicineOrder.getVolume());
      response.setTakePerDay(medicineOrder.getTakePerDay());
      response.setTakeDate(medicineOrder.getTakeDate());
    } else if (baseOrder.getOrder().equals(Order.TEST)) {
      TestOrder testOrder = (TestOrder) baseOrder;
      response.setTest(testOrder.getTest());
    }

    return response;
  }
}
