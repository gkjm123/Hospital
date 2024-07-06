package com.example.hospital.controller.patient;

import com.example.hospital.dto.response.doctor.BaseOrderResponse;
import com.example.hospital.dto.response.doctor.MedicineOrderResponse;
import com.example.hospital.service.patient.AdmService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/patient/adm")
@RequiredArgsConstructor
public class AdmController {
    private final AdmService admService;

    @PreAuthorize("hasRole('PATIENT')")
    @GetMapping("/get-orders")
    public List<BaseOrderResponse> getOrders(@RequestHeader(name = "TOKEN") String token) {
        return admService.getOrders(token);
    }

    //약 타기(불출)
    @PreAuthorize("hasRole('PATIENT')")
    @PutMapping("/take-medicine")
    public MedicineOrderResponse takeMedicine(@RequestHeader(name = "TOKEN") String token, @RequestParam(name = "order-id") Long orderId) {
        return admService.takeMedicine(token, orderId);
    }
}