package com.example.hospital.dto.response.doctor;

import com.example.hospital.entity.order.MedicineOrder;
import com.example.hospital.type.Medicine;
import com.example.hospital.type.OrderStatus;
import com.example.hospital.type.Order;
import com.example.hospital.type.TakePerDay;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MedicineOrderResponse {

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

  public static MedicineOrderResponse fromEntity(MedicineOrder medicineOrder) {
    return MedicineOrderResponse.builder()
        .id(medicineOrder.getId())
        .registId(medicineOrder.getRegist().getId())
        .order(medicineOrder.getOrder())
        .orderStatus(medicineOrder.getOrderStatus())
        .cost(medicineOrder.getCost())
        .orderCreateTime(medicineOrder.getOrderCreateTime())
        .orderStartDate(medicineOrder.getOrderStartTime())
        .medicine(medicineOrder.getMedicine())
        .volume(medicineOrder.getVolume())
        .takePerDay(medicineOrder.getTakePerDay())
        .takeDate(medicineOrder.getTakeDate())
        .build();
  }
}
