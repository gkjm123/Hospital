package com.example.hospital.controller.doctor;

import com.example.hospital.dto.form.doctor.MedicineOrderForm;
import com.example.hospital.dto.form.doctor.TestOrderForm;
import com.example.hospital.dto.response.doctor.BaseOrderResponse;
import com.example.hospital.dto.response.doctor.MedicineOrderResponse;
import com.example.hospital.dto.response.doctor.TestOrderResponse;
import com.example.hospital.dto.response.patient.RegistResponse;
import com.example.hospital.service.doctor.OrderService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/doctor/order")
@RequiredArgsConstructor
public class OrderController {

  private final OrderService orderService;

  //자신에게 접수된 환자건 조회
  @PreAuthorize("hasRole('DOCTOR')")
  @GetMapping("/registered-patient")
  public Page<RegistResponse> getRegistered(@PageableDefault Pageable pageable) {

    return orderService.getRegistered(pageable);
  }

  //약 처방하기
  @PreAuthorize("hasRole('DOCTOR')")
  @PostMapping("/medicine")
  public MedicineOrderResponse orderMedicine(@RequestBody MedicineOrderForm form) {

    return orderService.orderMedicine(form);
  }

  //검사 처방하기
  @PreAuthorize("hasRole('DOCTOR')")
  @PostMapping("/test")
  public TestOrderResponse orderTest(@RequestBody TestOrderForm form) {

    return orderService.orderTest(form);
  }

  //접수건에 대한 모든 처방 확인
  @PreAuthorize("hasRole('DOCTOR')")
  @GetMapping("/orders/{regist-id}")
  public List<BaseOrderResponse> getOrders(
      @PathVariable(name = "regist-id") Long registId,
      @PageableDefault(sort = "orderStartTime", direction = Sort.Direction.DESC) Pageable pageable
  ) {

    return orderService.getOrders(registId, pageable);
  }

  //처방 삭제(처방 수행이 완료되기 전까지만 가능)
  @PreAuthorize("hasRole('DOCTOR')")
  @DeleteMapping("/order/{order-id}")
  public String cancelOrder(@PathVariable(name = "order-id") Long orderId) {

    orderService.cancelOrder(orderId);
    return "처방 삭제 완료";
  }

  //퇴원 처방
  @PreAuthorize("hasRole('DOCTOR')")
  @PutMapping("/discharge/{regist-id}")
  public RegistResponse discharge(@PathVariable(name = "regist-id") Long registId) {
    return orderService.discharge(registId);
  }
}
