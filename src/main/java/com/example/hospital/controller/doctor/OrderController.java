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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
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
  @GetMapping("/get-regists")
  public Page<RegistResponse> getRegists(@RequestHeader(name = "TOKEN") String token,
      @RequestParam(name = "pageNumber") int pageNumber
  ) {
    return orderService.getRegists(token, pageNumber);
  }

  //약 처방하기
  @PreAuthorize("hasRole('DOCTOR')")
  @PostMapping("/order-medicine")
  public MedicineOrderResponse orderMedicine(@RequestHeader(name = "TOKEN") String token,
      @RequestBody MedicineOrderForm form
  ) {
    return orderService.orderMedicine(token, form);
  }

  //검사 처방하기
  @PreAuthorize("hasRole('DOCTOR')")
  @PostMapping("/order-test")
  public TestOrderResponse orderTest(@RequestHeader(name = "TOKEN") String token,
      @RequestBody TestOrderForm form
  ) {
    return orderService.orderTest(token, form);
  }

  //접수건에 대한 모든 처방 확인
  @PreAuthorize("hasRole('DOCTOR')")
  @GetMapping("/get-orders")
  public List<BaseOrderResponse> getOrders(@RequestHeader(name = "TOKEN") String token,
      @RequestParam(name = "regist-id") Long registId,
      @RequestParam(name = "pageNumber") int pageNumber
  ) {
    return orderService.getOrders(token, registId, pageNumber);
  }

  //처방 삭제(처방 수행이 완료되기 전까지만 가능)
  @PreAuthorize("hasRole('DOCTOR')")
  @DeleteMapping("/cancel-order")
  public String cancelOrder(@RequestHeader(name = "TOKEN") String token,
      @RequestParam(name = "order-id") Long orderId
  ) {
    orderService.cancelOrder(token, orderId);
    return "처방 삭제 완료";
  }

  //퇴원 처방
  @PreAuthorize("hasRole('DOCTOR')")
  @PutMapping("/discharge")
  public RegistResponse discharge(@RequestHeader(name = "TOKEN") String token,
      @RequestParam(name = "regist-id") Long registId
  ) {
    return orderService.discharge(token, registId);
  }
}
