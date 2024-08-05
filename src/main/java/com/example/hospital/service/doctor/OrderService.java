package com.example.hospital.service.doctor;

import com.example.hospital.dto.form.doctor.MedicineOrderForm;
import com.example.hospital.dto.form.doctor.TestOrderForm;
import com.example.hospital.dto.response.doctor.BaseOrderResponse;
import com.example.hospital.dto.response.doctor.MedicineOrderResponse;
import com.example.hospital.dto.response.doctor.TestOrderResponse;
import com.example.hospital.dto.response.patient.RegistResponse;
import com.example.hospital.entity.member.Doctor;
import com.example.hospital.entity.order.BaseOrder;
import com.example.hospital.entity.order.MedicineOrder;
import com.example.hospital.entity.order.TestOrder;
import com.example.hospital.entity.regist.Regist;
import com.example.hospital.exception.CustomException;
import com.example.hospital.exception.ErrorCode;
import com.example.hospital.repository.member.DoctorRepository;
import com.example.hospital.repository.order.BaseOrderRepository;
import com.example.hospital.repository.order.MedicineOrderRepository;
import com.example.hospital.repository.order.TestOrderRepository;
import com.example.hospital.repository.regist.RegistRepository;
import com.example.hospital.security.JwtProvider;
import com.example.hospital.type.OrderStatus;
import com.example.hospital.type.Order;
import com.example.hospital.type.RegisterStatus;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderService {

  private final JwtProvider jwtProvider;
  private final DoctorRepository doctorRepository;
  private final RegistRepository registRepository;
  private final BaseOrderRepository baseOrderRepository;
  private final MedicineOrderRepository medicineOrderRepository;
  private final TestOrderRepository testOrderRepository;

  @Transactional(readOnly = true)
  public Page<RegistResponse> getRegistered(Pageable pageable) {

    Doctor doctor = getDoctor();

    //해당 의사에게 등록된 접수건 목록 반환
    Page<Regist> regists = registRepository.
        findAllByDoctor_IdAndRegistType(doctor.getId(), RegisterStatus.REGISTERED, pageable);

    if (regists.isEmpty()) {
      throw new CustomException(ErrorCode.LIST_EMPTY);
    }

    return regists.map(RegistResponse::fromEntity);
  }

  @Transactional
  public MedicineOrderResponse orderMedicine(MedicineOrderForm form) {

    Doctor doctor = getDoctor();

    Regist regist = registRepository.findById(form.getRegistId())
        .orElseThrow(() -> new CustomException(ErrorCode.REGIST_NOT_FOUND));

    //접수건의 담당 의사가 맞는지 체크
    if (!regist.getDoctor().getId().equals(doctor.getId())) {
      throw new CustomException(ErrorCode.DOCTOR_NOT_MATCH);
    }

    MedicineOrder medicineOrder = MedicineOrder.builder()
        .regist(regist)
        .order(Order.MEDICINE)
        .orderStatus(OrderStatus.ORDERED)
        .orderStartTime(form.getOrderStartDate())
        .medicine(form.getMedicine())
        .volume(form.getVolume())
        .takePerDay(form.getTakePerDay())
        .takeDate(form.getTakeDate())
        .build();

    //약 비용 = 한알당 가격 * 일회 복용량 * 하루 복용횟수 * 복용일수
    Long cost = form.getMedicine().getCost() * form.getVolume() * form.getTakePerDay().getTime()
        * form.getTakeDate();

    medicineOrder.setCost(cost);

    return MedicineOrderResponse.fromEntity(medicineOrderRepository.save(medicineOrder));
  }

  @Transactional
  public TestOrderResponse orderTest(TestOrderForm form) {

    Doctor doctor = getDoctor();

    Regist regist = registRepository.findById(form.getRegistId())
        .orElseThrow(() -> new CustomException(ErrorCode.ID_PASSWORD_INVALID));

    if (!regist.getDoctor().getId().equals(doctor.getId())) {
      throw new CustomException(ErrorCode.DOCTOR_NOT_MATCH);
    }

    TestOrder testOrder = TestOrder.builder()
        .regist(regist)
        .order(Order.TEST)
        .orderStatus(OrderStatus.ORDERED)
        .orderStartTime(form.getOrderStartDate())
        .test(form.getTest())
        .build();

    //비용 세팅
    testOrder.setCost(form.getTest().getCost());

    return TestOrderResponse.fromEntity(testOrderRepository.save(testOrder));
  }

  @Transactional(readOnly = true)
  public List<BaseOrderResponse> getOrders(Long registId, Pageable pageable) {

    Doctor doctor = getDoctor();

    Regist regist = registRepository.findById(registId)
        .orElseThrow(() -> new CustomException(ErrorCode.ID_PASSWORD_INVALID));

    if (!regist.getDoctor().getId().equals(doctor.getId())) {
      throw new CustomException(ErrorCode.DOCTOR_NOT_MATCH);
    }

    Page<BaseOrder> orders = baseOrderRepository.findAllByRegist_Id(registId, pageable);

    if (orders.isEmpty()) {
      throw new CustomException(ErrorCode.LIST_EMPTY);
    }

    return orders.stream().map(BaseOrderResponse::fromEntity).toList();
  }

  @Transactional
  public void cancelOrder(Long orderId) {

    Doctor doctor = getDoctor();

    BaseOrder order = baseOrderRepository.findById(orderId)
        .orElseThrow(() -> new CustomException(ErrorCode.ORDER_NOT_FOUND));

    if (!order.getRegist().getDoctor().getId().equals(doctor.getId())) {
      throw new CustomException(ErrorCode.DOCTOR_NOT_MATCH);
    }

    //진행중인 접수건에 대해서만 처방 삭제 가능
    if (!order.getRegist().getRegisterStatus().equals(RegisterStatus.REGISTERED)) {
      throw new CustomException(ErrorCode.REGIST_STATUS_NOT_PRESENT);
    }

    //약이 불출 되었거나 검사가 진행 되었으면 처방 삭제 불가
    if (order.getOrderStatus().equals(OrderStatus.COMPLETED)) {
      throw new CustomException(ErrorCode.ORDER_COMPLETED);
    }

    baseOrderRepository.delete(order);
  }

  @Transactional
  public RegistResponse discharge(Long registId) {

    Doctor doctor = getDoctor();

    Regist regist = registRepository.findById(registId)
        .orElseThrow(() -> new CustomException(ErrorCode.ID_PASSWORD_INVALID));

    if (!regist.getDoctor().getId().equals(doctor.getId())) {
      throw new CustomException(ErrorCode.DOCTOR_NOT_MATCH);
    }

    //진행중인 접수건에 대해서만 퇴원 처방 가능
    if (!regist.getRegisterStatus().equals(RegisterStatus.REGISTERED)) {
      throw new CustomException(ErrorCode.REGIST_STATUS_NOT_PRESENT);
    }

    //총 입원료 계산
    Long cost = baseOrderRepository.findAll().stream().mapToLong(BaseOrder::getCost).sum();
    regist.setCost(cost);

    //접수건의 상태를 퇴원 대기중으로 바꾼다. 이 상태에서만 환자가 정산 가능
    regist.setRegisterStatus(RegisterStatus.WAIT_FOR_PAY);

    return RegistResponse.fromEntity(registRepository.save(regist));
  }

  private Doctor getDoctor() {
    return (Doctor) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
  }
}
