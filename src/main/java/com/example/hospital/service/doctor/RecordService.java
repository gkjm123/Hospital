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
import com.example.hospital.security.SecurityManager;
import com.example.hospital.type.OrderStatusType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RecordService {
    private final SecurityManager securityManager;
    private final DoctorRepository doctorRepository;
    private final RegistRepository registRepository;
    private final TestOrderRepository testOrderRepository;
    private final TestRecordRepository testRecordRepository;
    private final OpinionRepository opinionRepository;
    private final TestRecordRepository recordRepository;

    @Transactional
    public TestRecordResponse doTest(String token, TestRecordForm form) {
        Doctor doctor = doctorRepository.findByLoginId(securityManager.parseToken(token).getSubject()).get();

        TestOrder testOrder = testOrderRepository.findById(form.getTestOrderId())
                .orElseThrow(() -> new CustomException(ErrorCode.ORDER_NOT_FOUND));

        if (!testOrder.getRegist().getDoctor().getId().equals(doctor.getId())) {
            throw new CustomException(ErrorCode.DOCTOR_NOT_MATCH);
        }

        //검사를 시행 완료함. 처방의 타입을 Completed 로 변경
        testOrder.setOrderStatusType(OrderStatusType.COMPLETED);

        //검사 결과를 폼에서 읽어와 세팅후 저장
        TestRecord testRecord = TestRecord.builder()
                .testOrder(testOrder)
                .result(form.getResult())
                .testDate(LocalDateTime.now())
                .build();

        return TestRecordResponse.fromEntity(testRecordRepository.save(testRecord));
    }

    @Transactional
    public List<TestRecordResponse> getTestRecords(String token, Long testOrderId) {
        Doctor doctor = doctorRepository.findByLoginId(securityManager.parseToken(token).getSubject()).get();

        TestOrder order = testOrderRepository.findById(testOrderId)
                .orElseThrow(() -> new CustomException(ErrorCode.ORDER_NOT_FOUND));

        if (!order.getRegist().getDoctor().getId().equals(doctor.getId())) {
            throw new CustomException(ErrorCode.DOCTOR_NOT_MATCH);
        }

        List<TestRecord> records = testRecordRepository.findAllByTestOrder_Id(testOrderId);

        if (records.isEmpty()) {
            throw new CustomException(ErrorCode.LIST_EMPTY);
        }

        return records.stream().map(TestRecordResponse::fromEntity).toList();
    }

    @Transactional
    public TestRecordResponse updateTestRecord(String token, Long testRecordId, TestRecordForm form) {
        Doctor doctor = doctorRepository.findByLoginId(securityManager.parseToken(token).getSubject()).get();

        TestRecord record = testRecordRepository.findById(testRecordId)
                .orElseThrow(() -> new CustomException(ErrorCode.RECORD_NOT_FOUND));

        if (!record.getTestOrder().getRegist().getDoctor().getId().equals(doctor.getId())) {
            throw new CustomException(ErrorCode.DOCTOR_NOT_MATCH);
        }

        record.setResult(form.getResult());
        return TestRecordResponse.fromEntity(recordRepository.save(record));
    }

    @Transactional
    public RegistResponse diagnosis(String token, DiagnosisForm form) {
        Doctor doctor = doctorRepository.findByLoginId(securityManager.parseToken(token).getSubject()).get();

        Regist regist = registRepository.findById(form.getRegistId())
                .orElseThrow(() -> new CustomException(ErrorCode.ID_PASSWORD_INVALID));

        if (!regist.getDoctor().getId().equals(doctor.getId())) {
            throw new CustomException(ErrorCode.DOCTOR_NOT_MATCH);
        }

        //입원시 null 로 세팅되었던 진단명 항목에 폼의 값을 넣어줌
        regist.setDiagnosis(form.getName());
        return RegistResponse.fromEntity(registRepository.save(regist));
    }

    @Transactional
    public OpinionResponse makeOpinion(String token, OpinionForm form) {
        Doctor doctor = doctorRepository.findByLoginId(securityManager.parseToken(token).getSubject()).get();

        Regist regist = registRepository.findById(form.getRegistId())
                .orElseThrow(() -> new CustomException(ErrorCode.ID_PASSWORD_INVALID));

        if (!regist.getDoctor().getId().equals(doctor.getId())) {
            throw new CustomException(ErrorCode.DOCTOR_NOT_MATCH);
        }

        //폼에서 값을 읽어와 소견서 세팅
        Opinion opinion = Opinion.builder()
                .regist(regist)
                .opinion(form.getOpinion())
                .build();

        return OpinionResponse.fromEntity(opinionRepository.save(opinion));
    }

    @Transactional
    public OpinionResponse updateOpinion(String token, Long opinionId, OpinionForm form) {
        Doctor doctor = doctorRepository.findByLoginId(securityManager.parseToken(token).getSubject()).get();

        Opinion record = opinionRepository.findById(opinionId)
                .orElseThrow(() -> new CustomException(ErrorCode.RECORD_NOT_FOUND));

        if (record.getRegist().getDoctor().getId().equals(doctor.getId())) {
            throw new CustomException(ErrorCode.DOCTOR_NOT_MATCH);
        }

        record.setOpinion(form.getOpinion());
        return OpinionResponse.fromEntity(opinionRepository.save(record));
    }

    @Transactional
    public void deleteOpinion(String token, Long opinionId) {
        Doctor doctor = doctorRepository.findByLoginId(securityManager.parseToken(token).getSubject()).get();

        Opinion record = opinionRepository.findById(opinionId)
                .orElseThrow(() -> new CustomException(ErrorCode.RECORD_NOT_FOUND));

        if (record.getRegist().getDoctor().getId().equals(doctor.getId())) {
            throw new CustomException(ErrorCode.DOCTOR_NOT_MATCH);
        }

        opinionRepository.delete(record);
    }
}
