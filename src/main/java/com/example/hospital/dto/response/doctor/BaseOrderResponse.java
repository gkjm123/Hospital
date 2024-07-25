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
  private OrderType orderType;
  private OrderStatusType orderStatusType;
  private Long cost;
  private LocalDateTime orderCreateTime;
  private LocalDate orderStartDate;

  private MedicineType medicineType;
  private Long volume;
  private TakeType takeType;
  private Long takeDate;

  private TestType testType;

  public static BaseOrderResponse fromEntity(BaseOrder baseOrder) {
    BaseOrderResponse response = BaseOrderResponse.builder()
        .id(baseOrder.getId())
        .registId(baseOrder.getRegist().getId())
        .orderType(baseOrder.getOrderType())
        .orderStatusType(baseOrder.getOrderStatusType())
        .cost(baseOrder.getCost())
        .orderCreateTime(baseOrder.getOrderCreateTime())
        .orderStartDate(baseOrder.getOrderStartTime())
        .build();

    if (baseOrder.getOrderType().equals(OrderType.MEDICINE)) {
      MedicineOrder medicineOrder = (MedicineOrder) baseOrder;

      response.setMedicineType(medicineOrder.getMedicineType());
      response.setVolume(medicineOrder.getVolume());
      response.setTakeType(medicineOrder.getTakeType());
      response.setTakeDate(medicineOrder.getTakeDate());
    } else if (baseOrder.getOrderType().equals(OrderType.TEST)) {
      TestOrder testOrder = (TestOrder) baseOrder;
      response.setTestType(testOrder.getTestType());
    }

    return response;
  }
}
