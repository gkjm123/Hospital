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
import com.example.hospital.security.SecurityManager;
import com.example.hospital.type.OrderStatusType;
import com.example.hospital.type.OrderType;
import com.example.hospital.type.RegistType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final SecurityManager securityManager;
    private final DoctorRepository doctorRepository;
    private final RegistRepository registRepository;
    private final BaseOrderRepository baseOrderRepository;
    private final MedicineOrderRepository medicineOrderRepository;
    private final TestOrderRepository testOrderRepository;

    @Transactional
    public List<RegistResponse> getRegists(String token) {
        Doctor doctor = doctorRepository.findByLoginId(securityManager.parseToken(token).getSubject()).get();

        List<Regist> regists = registRepository.findAllByDoctor_IdAndRegistType(doctor.getId(), RegistType.REGISTERED);

        if (regists.isEmpty()) {
            throw new CustomException(ErrorCode.LIST_EMPTY);
        }

        return regists.stream().map(RegistResponse::fromEntity).toList();
    }

    @Transactional
    public MedicineOrderResponse orderMedicine(String token, MedicineOrderForm form) {
        Doctor doctor = doctorRepository.findByLoginId(securityManager.parseToken(token).getSubject()).get();

        Regist regist = registRepository.findById(form.getRegistId())
                .orElseThrow(() -> new CustomException(ErrorCode.REGIST_NOT_FOUND));

        if (!regist.getDoctor().getId().equals(doctor.getId())) {
            throw new CustomException(ErrorCode.DOCTOR_NOT_MATCH);
        }

        //약 처방하기: 약 종류,타입,복용량 등을 폼에서 읽어와서 세팅한다.
        MedicineOrder medicineOrder = MedicineOrder.builder()
                .regist(regist)
                .orderType(OrderType.MEDICINE)
                .orderStatusType(OrderStatusType.ORDERED)
                .orderStartTime(form.getOrderStartDate())
                .medicineType(form.getMedicineType())
                .volume(form.getVolume())
                .takeType(form.getTakeType())
                .takeDate(form.getTakeDate())
                .build();

        //약 비용 = 한알당 가격 * 일회 복용량 * 하루 복용횟수 * 복용일수
        Long cost = form.getMedicineType().getCost() * form.getVolume() * form.getTakeType().getTime() * form.getTakeDate();
        medicineOrder.setCost(cost);

        return MedicineOrderResponse.fromEntity(medicineOrderRepository.save(medicineOrder));
    }

    @Transactional
    public TestOrderResponse orderTest(String token, TestOrderForm form) {
        Doctor doctor = doctorRepository.findByLoginId(securityManager.parseToken(token).getSubject()).get();

        Regist regist = registRepository.findById(form.getRegistId())
                .orElseThrow(() -> new CustomException(ErrorCode.ID_PASSWORD_INVALID));

        if (!regist.getDoctor().getId().equals(doctor.getId())) {
            throw new CustomException(ErrorCode.DOCTOR_NOT_MATCH);
        }

        TestOrder testOrder = TestOrder.builder()
                .regist(regist)
                .orderType(OrderType.TEST)
                .orderStatusType(OrderStatusType.ORDERED)
                .orderStartTime(form.getOrderStartDate())
                .testType(form.getTestType())
                .build();

        //검사는 타입에 따라 비용이 정해져있고 특별한 추가 계산이 필요 없다.
        testOrder.setCost(form.getTestType().getCost());
        return TestOrderResponse.fromEntity(testOrderRepository.save(testOrder));
    }

    @Transactional
    public List<BaseOrderResponse> getOrders(String token, Long registId) {
        Doctor doctor = doctorRepository.findByLoginId(securityManager.parseToken(token).getSubject()).get();

        Regist regist = registRepository.findById(registId)
                .orElseThrow(() -> new CustomException(ErrorCode.ID_PASSWORD_INVALID));

        if (!regist.getDoctor().getId().equals(doctor.getId())) {
            throw new CustomException(ErrorCode.DOCTOR_NOT_MATCH);
        }

        List<BaseOrder> orders = baseOrderRepository.findAllByRegist_IdOrderByOrderStartTimeDesc(registId);

        if (orders.isEmpty()) {
            throw new CustomException(ErrorCode.LIST_EMPTY);
        }

        return orders.stream().map(BaseOrderResponse::fromEntity).toList();
    }

    @Transactional
    public void cancelOrder(String token, Long orderId) {
        Doctor doctor = doctorRepository.findByLoginId(securityManager.parseToken(token).getSubject()).get();

        BaseOrder order = baseOrderRepository.findById(orderId)
                .orElseThrow(() -> new CustomException(ErrorCode.ORDER_NOT_FOUND));

        if (!order.getRegist().getDoctor().getId().equals(doctor.getId())) {
            throw new CustomException(ErrorCode.DOCTOR_NOT_MATCH);
        }

        //진행중인 접수건에 대해서만 처방 삭제 가능
        if (!order.getRegist().getRegistType().equals(RegistType.REGISTERED)) {
            throw new CustomException(ErrorCode.REGIST_STATUS_NOT_PRESENT);
        }

        //약이 불출되었거나 검사가 진행되었으면 오더 타입이 Completed 로 변경되며 이때는 처방 삭제 불가
        if (order.getOrderStatusType().equals(OrderStatusType.COMPLETED)) {
            throw new CustomException(ErrorCode.ORDER_COMPLETED);
        }

        baseOrderRepository.delete(order);
    }

    @Transactional
    public RegistResponse discharge(String token, Long registId) {
        Doctor doctor = doctorRepository.findByLoginId(securityManager.parseToken(token).getSubject()).get();

        Regist regist = registRepository.findById(registId)
                .orElseThrow(() -> new CustomException(ErrorCode.ID_PASSWORD_INVALID));

        if (!regist.getDoctor().getId().equals(doctor.getId())) {
            throw new CustomException(ErrorCode.DOCTOR_NOT_MATCH);
        }

        //진행중인 접수건에 대해서만 퇴원 처방 가능
        if (!regist.getRegistType().equals(RegistType.REGISTERED)) {
            throw new CustomException(ErrorCode.REGIST_STATUS_NOT_PRESENT);
        }

        //퇴원하는 접수건에 등록된 모든 처방(약, 검사)의 비용을 합산한 최종 입원료를 접수건에 업데이트
        Long cost = baseOrderRepository.findAll().stream().mapToLong(BaseOrder::getCost).sum();
        regist.setCost(cost);

        //접수건의 상태를 퇴원 대기중으로 바꾼다. 이 상태에서만 환자가 정산 가능
        regist.setRegistType(RegistType.WAIT_FOR_PAY);

        return RegistResponse.fromEntity(registRepository.save(regist));
    }
}
