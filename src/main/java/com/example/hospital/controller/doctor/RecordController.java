package com.example.hospital.controller.doctor;

import com.example.hospital.dto.form.doctor.DiagnosisForm;
import com.example.hospital.dto.form.doctor.OpinionForm;
import com.example.hospital.dto.form.doctor.TestRecordForm;
import com.example.hospital.dto.response.doctor.OpinionResponse;
import com.example.hospital.dto.response.doctor.TestRecordResponse;
import com.example.hospital.dto.response.patient.RegistResponse;
import com.example.hospital.service.doctor.RecordService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/doctor/record")
@RequiredArgsConstructor
public class RecordController {

  private final RecordService recordService;

  //검사 및 검사결과 기록하기
  @PreAuthorize("hasRole('DOCTOR')")
  @PostMapping("/test")
  public TestRecordResponse doTest(@RequestBody TestRecordForm form) {

    return recordService.doTest(form);
  }

  //검사 결과 확인
  @PreAuthorize("hasRole('DOCTOR')")
  @GetMapping("/tests/{test-order-id}")
  public List<TestRecordResponse> getTestRecords(
      @PathVariable(name = "test-order-id") Long testOrderId
  ) {

    return recordService.getTestRecords(testOrderId);
  }

  //검사 결과 수정
  @PreAuthorize("hasRole('DOCTOR')")
  @PutMapping("/test/{test-record-id}")
  public TestRecordResponse updateTestRecord(
      @PathVariable(name = "test-record-id") Long testRecordId,
      @RequestBody TestRecordForm form
  ) {

    return recordService.updateTestRecord(testRecordId, form);
  }

  //진단명 입력 또는 수정
  @PreAuthorize("hasRole('DOCTOR')")
  @PutMapping("/diagnosis")
  public RegistResponse diagnosis(@RequestBody DiagnosisForm form) {

    return recordService.diagnosis(form);
  }

  //소견서 작성하기
  @PreAuthorize("hasRole('DOCTOR')")
  @PostMapping("/opinion")
  public OpinionResponse makeOpinion(@RequestBody OpinionForm form) {
    return recordService.makeOpinion(form);
  }

  //소견서 수정하기
  @PreAuthorize("hasRole('DOCTOR')")
  @PutMapping("/opinion/{opinion-id}")
  public OpinionResponse updateOpinion(
      @PathVariable(name = "opinion-id") Long opinionId,
      @RequestBody OpinionForm form
  ) {

    return recordService.updateOpinion(opinionId, form);
  }

  //소견서 삭제하기
  @PreAuthorize("hasRole('DOCTOR')")
  @DeleteMapping("/opinion/{opinion-id}")
  public String deleteOpinion(@PathVariable(name = "opinion-id") Long opinionId) {

    recordService.deleteOpinion(opinionId);
    return "소견서 삭제 완료";
  }
}
