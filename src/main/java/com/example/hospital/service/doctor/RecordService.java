package com.example.hospital.service.doctor;

import com.example.hospital.dto.form.doctor.DiagnosisForm;
import com.example.hospital.dto.form.doctor.OpinionForm;
import com.example.hospital.dto.form.doctor.TestRecordForm;
import com.example.hospital.dto.response.doctor.OpinionResponse;
import com.example.hospital.dto.response.doctor.TestRecordResponse;
import com.example.hospital.dto.response.patient.RegistResponse;
import com.example.hospital.entity.member.Doctor;
import com.example.hospital.entity.order.TestOrder;
import com.example.hospital.entity.record.Opinion;
import com.example.hospital.entity.record.TestRecord;
import com.example.hospital.entity.regist.Regist;
import com.example.hospital.exception.CustomException;
import com.example.hospital.exception.ErrorCode;
import com.example.hospital.repository.member.DoctorRepository;
import com.example.hospital.repository.order.TestOrderRepository;
import com.example.hospital.repository.record.OpinionRepository;
import com.example.hospital.repository.record.TestRecordRepository;
import com.example.hospital.repository.regist.RegistRepository;
import com.example.hospital.security.JwtProvider;
import com.example.hospital.type.OrderStatus;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RecordService {

  private final JwtProvider jwtProvider;
  private final DoctorRepository doctorRepository;
  private final RegistRepository registRepository;
  private final TestOrderRepository testOrderRepository;
  private final TestRecordRepository testRecordRepository;
  private final OpinionRepository opinionRepository;
  private final TestRecordRepository recordRepository;

  @Transactional
  public TestRecordResponse doTest(TestRecordForm form) {
    Doctor doctor = getDoctor();

    TestOrder testOrder = testOrderRepository.findById(form.getTestOrderId())
        .orElseThrow(() -> new CustomException(ErrorCode.ORDER_NOT_FOUND));

    if (!testOrder.getRegist().getDoctor().getId().equals(doctor.getId())) {
      throw new CustomException(ErrorCode.DOCTOR_NOT_MATCH);
    }

    //검사 시행 완료
    testOrder.setOrderStatus(OrderStatus.COMPLETED);

    TestRecord testRecord = TestRecord.builder()
        .testOrder(testOrder)
        .result(form.getResult())
        .testDate(LocalDateTime.now())
        .build();

    return TestRecordResponse.fromEntity(testRecordRepository.save(testRecord));
  }

  @Transactional(readOnly = true)
  public List<TestRecordResponse> getTestRecords(Long testOrderId) {

    Doctor doctor = getDoctor();

    TestOrder order = testOrderRepository.findById(testOrderId)
        .orElseThrow(() -> new CustomException(ErrorCode.ORDER_NOT_FOUND));

    if (!order.getRegist().getDoctor().getId().equals(doctor.getId())) {
      throw new CustomException(ErrorCode.DOCTOR_NOT_MATCH);
    }

    //testOrder ID 로 입력된 검사 기록 반환
    List<TestRecord> records = testRecordRepository.findAllByTestOrder_Id(testOrderId);

    if (records.isEmpty()) {
      throw new CustomException(ErrorCode.LIST_EMPTY);
    }

    return records.stream().map(TestRecordResponse::fromEntity).toList();
  }

  @Transactional
  public TestRecordResponse updateTestRecord(Long testRecordId, TestRecordForm form) {

    Doctor doctor = getDoctor();

    TestRecord record = testRecordRepository.findById(testRecordId)
        .orElseThrow(() -> new CustomException(ErrorCode.RECORD_NOT_FOUND));

    if (!record.getTestOrder().getRegist().getDoctor().getId().equals(doctor.getId())) {
      throw new CustomException(ErrorCode.DOCTOR_NOT_MATCH);
    }

    //테스트 결과 입력
    record.setResult(form.getResult());

    return TestRecordResponse.fromEntity(recordRepository.save(record));
  }

  @Transactional
  public RegistResponse diagnosis(DiagnosisForm form) {
    Doctor doctor = getDoctor();

    Regist regist = registRepository.findById(form.getRegistId())
        .orElseThrow(() -> new CustomException(ErrorCode.ID_PASSWORD_INVALID));

    if (!regist.getDoctor().getId().equals(doctor.getId())) {
      throw new CustomException(ErrorCode.DOCTOR_NOT_MATCH);
    }

    //진단명 세팅
    regist.setDiagnosis(form.getName());

    return RegistResponse.fromEntity(registRepository.save(regist));
  }

  @Transactional
  public OpinionResponse makeOpinion(OpinionForm form) {

    Doctor doctor = getDoctor();

    Regist regist = registRepository.findById(form.getRegistId())
        .orElseThrow(() -> new CustomException(ErrorCode.ID_PASSWORD_INVALID));

    if (!regist.getDoctor().getId().equals(doctor.getId())) {
      throw new CustomException(ErrorCode.DOCTOR_NOT_MATCH);
    }

    Opinion opinion = Opinion.builder()
        .regist(regist)
        .opinion(form.getOpinion())
        .build();

    return OpinionResponse.fromEntity(opinionRepository.save(opinion));
  }

  @Transactional
  public OpinionResponse updateOpinion(Long opinionId, OpinionForm form) {

    Doctor doctor = getDoctor();

    Opinion record = opinionRepository.findById(opinionId)
        .orElseThrow(() -> new CustomException(ErrorCode.RECORD_NOT_FOUND));

    if (record.getRegist().getDoctor().getId().equals(doctor.getId())) {
      throw new CustomException(ErrorCode.DOCTOR_NOT_MATCH);
    }

    record.setOpinion(form.getOpinion());

    return OpinionResponse.fromEntity(opinionRepository.save(record));
  }

  @Transactional
  public void deleteOpinion(Long opinionId) {

    Doctor doctor = getDoctor();

    Opinion record = opinionRepository.findById(opinionId)
        .orElseThrow(() -> new CustomException(ErrorCode.RECORD_NOT_FOUND));

    if (record.getRegist().getDoctor().getId().equals(doctor.getId())) {
      throw new CustomException(ErrorCode.DOCTOR_NOT_MATCH);
    }

    opinionRepository.delete(record);
  }

  private Doctor getDoctor() {
    return (Doctor) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
  }
}
