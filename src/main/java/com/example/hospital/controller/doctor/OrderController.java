package com.example.hospital.controller.doctor;

import com.example.hospital.dto.form.doctor.MedicineOrderForm;
import com.example.hospital.dto.form.doctor.TestOrderForm;
import com.example.hospital.dto.response.doctor.BaseOrderResponse;
import com.example.hospital.dto.response.doctor.MedicineOrderResponse;
import com.example.hospital.dto.response.doctor.TestOrderResponse;
import com.example.hospital.dto.response.patient.RegistResponse;
import com.example.hospital.service.doctor.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/doctor/order")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @PreAuthorize("hasRole('DOCTOR')")
    @GetMapping("/get-regists")
    public List<RegistResponse> getRegists(@RequestHeader(name = "TOKEN") String token) {
        return orderService.getRegists(token);
    }

    @PreAuthorize("hasRole('DOCTOR')")
    @PostMapping("/order-medicine")
    public MedicineOrderResponse orderMedicine(@RequestHeader(name = "TOKEN") String token,
                                               @RequestBody MedicineOrderForm form
    ) {
        return orderService.orderMedicine(token, form);
    }

    @PreAuthorize("hasRole('DOCTOR')")
    @PostMapping("/order-test")
    public TestOrderResponse orderTest(@RequestHeader(name = "TOKEN") String token,
                                       @RequestBody TestOrderForm form
    ) {
        return orderService.orderTest(token, form);
    }

    @PreAuthorize("hasRole('DOCTOR')")
    @GetMapping("/get-orders")
    public List<BaseOrderResponse> getOrders(@RequestHeader(name = "TOKEN") String token,
                                             @RequestParam(name = "regist-id") Long registId
    ) {
        return orderService.getOrders(token, registId);
    }

    @PreAuthorize("hasRole('DOCTOR')")
    @DeleteMapping("/cancel-order")
    public String cancelOrder(@RequestHeader(name = "TOKEN") String token,
                              @RequestParam(name = "order-id") Long orderId
    ) {
        orderService.cancelOrder(token, orderId);
        return "처방 삭제 완료";
    }

    @PreAuthorize("hasRole('DOCTOR')")
    @PutMapping("/discharge")
    public RegistResponse discharge(@RequestHeader(name = "TOKEN") String token,
                                    @RequestParam(name = "regist-id") Long registId
    ) {
        return orderService.discharge(token, registId);
    }
}
