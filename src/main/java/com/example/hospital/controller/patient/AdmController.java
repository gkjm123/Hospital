package com.example.hospital.controller.patient;

import com.example.hospital.dto.response.doctor.BaseOrderResponse;
import com.example.hospital.dto.response.doctor.MedicineOrderResponse;
import com.example.hospital.service.patient.AdmService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/patient/adm")
@RequiredArgsConstructor
public class AdmController {

  private final AdmService admService;

  //본인에게 난 모든 처방 확인하기(약 또는 검사)
  @PreAuthorize("hasRole('PATIENT')")
  @GetMapping("/get-orders")
  public Page<BaseOrderResponse> getOrders(@RequestHeader(name = "TOKEN") String token,
      @RequestParam(name = "pageNumber") int pageNumber
  ) {
    return admService.getOrders(token, pageNumber);
  }

  //약 타기(불출)
  @PreAuthorize("hasRole('PATIENT')")
  @PutMapping("/take-medicine")
  public MedicineOrderResponse takeMedicine(@RequestHeader(name = "TOKEN") String token,
      @RequestParam(name = "order-id") Long orderId) {
    return admService.takeMedicine(token, orderId);
  }
}