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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AdmService {

  private final SecurityManager securityManager;
  private final PatientRepository patientRepository;
  private final RegistRepository registRepository;
  private final MedicineOrderRepository medicineOrderRepository;
  private final BaseOrderRepository baseOrderRepository;

  @Transactional(readOnly = true)
  public Page<BaseOrderResponse> getOrders(String token, int pageNumber) {
    Patient patient = patientRepository.findByLoginId(
        securityManager.parseToken(token).getSubject()).get();

    Regist regist = registRepository.findByPatient_IdAndRegistType(patient.getId(),
            RegistType.REGISTERED)
        .orElseThrow(() -> new CustomException(ErrorCode.REGIST_NOT_FOUND));

    Pageable pageable = PageRequest.of(pageNumber, 10);

    //본인 아이디로 처방된 모든 약, 검사 처방을 처방일이 최근인 순서로 반환
    Page<BaseOrder> orders = baseOrderRepository.findAllByRegist_IdOrderByOrderStartTimeDesc(
        regist.getId(), pageable);

    if (orders.isEmpty()) {
      throw new CustomException(ErrorCode.LIST_EMPTY);
    }

    return orders.map(BaseOrderResponse::fromEntity);
  }

  @Transactional
  public MedicineOrderResponse takeMedicine(String token, Long orderId) {
    Patient patient = patientRepository.findByLoginId(
        securityManager.parseToken(token).getSubject()).get();

    MedicineOrder order = medicineOrderRepository.findById(orderId)
        .orElseThrow(() -> new CustomException(ErrorCode.ORDER_NOT_FOUND));

    //본인에게 난 처방이 아닌 경우
    if (!order.getRegist().getPatient().getId().equals(patient.getId())) {
      throw new CustomException(ErrorCode.PATIENT_NOT_MATCH);
    }

    //진행중인 접수건에 대한 처방이 아닌 경우(ex.처방 ID가 이미 퇴원한 접수건에 대한 것임)
    if (!order.getRegist().getRegistType().equals(RegistType.REGISTERED)) {
      throw new CustomException(ErrorCode.REGIST_STATUS_NOT_PRESENT);
    }

    //이미 Completed 된 처방임(ex.이미 불출해간 약)
    if (!order.getOrderStatusType().equals(OrderStatusType.ORDERED)) {
      throw new CustomException(ErrorCode.ORDER_COMPLETED);
    }

    //약을 약국에서 타간것(불출)으로 취급. 처방을 Completed 상태로 바꿔준다.
    order.setOrderStatusType(OrderStatusType.COMPLETED);
    return MedicineOrderResponse.fromEntity(medicineOrderRepository.save(order));
  }
}
