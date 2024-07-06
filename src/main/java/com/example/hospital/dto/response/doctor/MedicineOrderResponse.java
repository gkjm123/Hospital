package com.example.hospital.dto.response.doctor;

import com.example.hospital.entity.order.MedicineOrder;
import com.example.hospital.type.MedicineType;
import com.example.hospital.type.OrderStatusType;
import com.example.hospital.type.OrderType;
import com.example.hospital.type.TakeType;
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
    private OrderType orderType;
    private OrderStatusType orderStatusType;
    private Long cost;
    private LocalDateTime orderCreateTime;
    private LocalDate orderStartDate;
    private MedicineType medicineType;
    private Long volume;
    private TakeType takeType;
    private Long takeDate;

    public static MedicineOrderResponse fromEntity(MedicineOrder medicineOrder) {
        return MedicineOrderResponse.builder()
                .id(medicineOrder.getId())
                .registId(medicineOrder.getRegist().getId())
                .orderType(medicineOrder.getOrderType())
                .orderStatusType(medicineOrder.getOrderStatusType())
                .cost(medicineOrder.getCost())
                .orderCreateTime(medicineOrder.getOrderCreateTime())
                .orderStartDate(medicineOrder.getOrderStartTime())
                .medicineType(medicineOrder.getMedicineType())
                .volume(medicineOrder.getVolume())
                .takeType(medicineOrder.getTakeType())
                .takeDate(medicineOrder.getTakeDate())
                .build();
    }
}
