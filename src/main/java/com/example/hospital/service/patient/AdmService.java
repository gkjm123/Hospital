package com.example.hospital.service.patient;

import com.example.hospital.dto.response.doctor.BaseOrderResponse;
import com.example.hospital.dto.response.doctor.MedicineOrderResponse;
import com.example.hospital.entity.member.Doctor;
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
import com.example.hospital.security.JwtProvider;
import com.example.hospital.type.OrderStatus;
import com.example.hospital.type.RegisterStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AdmService {

  private final JwtProvider jwtProvider;
  private final PatientRepository patientRepository;
  private final RegistRepository registRepository;
  private final MedicineOrderRepository medicineOrderRepository;
  private final BaseOrderRepository baseOrderRepository;

  @Transactional(readOnly = true)
  public Page<BaseOrderResponse> getOrders(Pageable pageable) {

    Patient patient = getPatient();

    Regist regist = registRepository
        .findByPatient_IdAndRegistType(patient.getId(), RegisterStatus.REGISTERED)
        .orElseThrow(() -> new CustomException(ErrorCode.REGIST_NOT_FOUND));

    //해당 접수건에 처방된 모든 처방 목록
    Page<BaseOrder> orders = baseOrderRepository.findAllByRegist_Id(regist.getId(), pageable);

    if (orders.isEmpty()) {
      throw new CustomException(ErrorCode.LIST_EMPTY);
    }

    return orders.map(BaseOrderResponse::fromEntity);
  }

  @Transactional
  public MedicineOrderResponse takeMedicine(Long orderId) {

    Patient patient = getPatient();

    MedicineOrder order = medicineOrderRepository.findById(orderId)
        .orElseThrow(() -> new CustomException(ErrorCode.ORDER_NOT_FOUND));

    //본인에게 난 처방인지 체크
    if (!order.getRegist().getPatient().getId().equals(patient.getId())) {
      throw new CustomException(ErrorCode.PATIENT_NOT_MATCH);
    }

    //진행중인 접수건에 대한 처방이 아닌 경우(ex.이미 퇴원한 접수건)
    if (!order.getRegist().getRegisterStatus().equals(RegisterStatus.REGISTERED)) {
      throw new CustomException(ErrorCode.REGIST_STATUS_NOT_PRESENT);
    }

    //이미 Completed 된 처방임(ex.이미 불출 해간 약)
    if (!order.getOrderStatus().equals(OrderStatus.ORDERED)) {
      throw new CustomException(ErrorCode.ORDER_COMPLETED);
    }

    //약을 불출함
    order.setOrderStatus(OrderStatus.COMPLETED);
    return MedicineOrderResponse.fromEntity(medicineOrderRepository.save(order));
  }

  private Patient getPatient() {
    return (Patient) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
  }
}
