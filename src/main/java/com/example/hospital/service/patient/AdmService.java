package com.example.hospital.service.patient;

import com.example.hospital.dto.response.doctor.BaseOrderResponse;
import com.example.hospital.dto.response.doctor.MedicineOrderResponse;
import com.example.hospital.entity.member.Patient;
import com.example.hospital.entity.order.BaseOrder;
import com.example.hospital.entity.order.MedicineOrder;
import com.example.hospital.entity.regist.Regist;
import com.example.hospital.exception.CustomException;
import com.example.hospital.exception.ErrorCode;
import com.example.hospital.repository.member.PatientRepository;
import com.example.hospital.repository.order.BaseOrderRepository;
import com.example.hospital.repository.order.MedicineOrderRepository;
import com.example.hospital.repository.regist.RegistRepository;
import com.example.hospital.security.SecurityManager;
import com.example.hospital.type.OrderStatusType;
import com.example.hospital.type.RegistType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdmService {
    private final SecurityManager securityManager;
    private final PatientRepository patientRepository;
    private final RegistRepository registRepository;
    private final MedicineOrderRepository medicineOrderRepository;
    private final BaseOrderRepository baseOrderRepository;

    @Transactional
    public List<BaseOrderResponse> getOrders(String token) {
        Patient patient = patientRepository.findByLoginId(securityManager.parseToken(token).getSubject()).get();

        Regist regist = registRepository.findByPatient_IdAndRegistType(patient.getId(), RegistType.REGISTERED)
                .orElseThrow(() -> new CustomException(ErrorCode.REGIST_NOT_FOUND));

        List<BaseOrder> orders = baseOrderRepository.findAllByRegist_IdOrderByOrderStartTimeDesc(regist.getId());

        if (orders.isEmpty()) {
            throw new CustomException(ErrorCode.LIST_EMPTY);
        }

        return orders.stream().map(BaseOrderResponse::fromEntity).toList();
    }

    @Transactional
    public MedicineOrderResponse takeMedicine(String token, Long orderId) {
        Patient patient = patientRepository.findByLoginId(securityManager.parseToken(token).getSubject()).get();

        MedicineOrder order = medicineOrderRepository.findById(orderId)
                .orElseThrow(() -> new CustomException(ErrorCode.ORDER_NOT_FOUND));

        if (!order.getRegist().getPatient().getId().equals(patient.getId())) {
            throw new CustomException(ErrorCode.PATIENT_NOT_MATCH);
        }

        if (!order.getRegist().getRegistType().equals(RegistType.REGISTERED)) {
            throw new CustomException(ErrorCode.REGIST_STATUS_NOT_PRESENT);
        }

        if (!order.getOrderStatusType().equals(OrderStatusType.ORDERED)) {
            throw new CustomException(ErrorCode.ORDER_COMPLETED);
        }

        order.setOrderStatusType(OrderStatusType.COMPLETED);
        return MedicineOrderResponse.fromEntity(medicineOrderRepository.save(order));
    }
}
