package com.example.hospital.controller.doctor;

import com.example.hospital.dto.form.doctor.DiagnosisForm;
import com.example.hospital.dto.form.doctor.OpinionForm;
import com.example.hospital.dto.form.doctor.TestRecordForm;
import com.example.hospital.dto.response.doctor.OpinionResponse;
import com.example.hospital.dto.response.doctor.TestRecordResponse;
import com.example.hospital.dto.response.patient.RegistResponse;
import com.example.hospital.service.doctor.RecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/doctor/record")
@RequiredArgsConstructor
public class RecordController {
    private final RecordService recordService;

    //검사 및 검사결과 기록하기
    @PreAuthorize("hasRole('DOCTOR')")
    @PostMapping("/do-test")
    public TestRecordResponse doTest(@RequestHeader(name = "TOKEN") String token,
                                     @RequestBody TestRecordForm form
    ) {
        return recordService.doTest(token, form);
    }

    //검사 결과 확인
    @PreAuthorize("hasRole('DOCTOR')")
    @GetMapping("/get-test-records")
    public List<TestRecordResponse> getTestRecords(@RequestHeader(name = "TOKEN") String token,
                                                   @RequestParam(name = "testorder-id") Long testOrderId
    ) {
        return recordService.getTestRecords(token, testOrderId);
    }

    //검사 결과 수정
    @PreAuthorize("hasRole('DOCTOR')")
    @PutMapping("/update-test-record")
    public TestRecordResponse updateTestRecord(@RequestHeader(name = "TOKEN") String token,
                                               @RequestParam(name = "testrecord-id") Long testRecordId,
                                               @RequestBody TestRecordForm form
    ) {
        return recordService.updateTestRecord(token, testRecordId, form);
    }

    //진단명 입력 또는 수정
    @PreAuthorize("hasRole('DOCTOR')")
    @PutMapping("/diagnosis")
    public RegistResponse diagnosis(@RequestHeader(name = "TOKEN") String token,
                                    @RequestBody DiagnosisForm form
    ) {
        return recordService.diagnosis(token, form);
    }

    //소견서 작성하기
    @PreAuthorize("hasRole('DOCTOR')")
    @PostMapping("/make-opinion")
    public OpinionResponse makeOpinion(@RequestHeader(name = "TOKEN") String token,
                                       @RequestBody OpinionForm form
    ) {
        return recordService.makeOpinion(token, form);
    }

    //소견서 수정하기
    @PreAuthorize("hasRole('DOCTOR')")
    @PutMapping("/update-opinion")
    public OpinionResponse updateOpinion(@RequestHeader(name = "TOKEN") String token,
                                         @RequestParam(name = "opinion-id") Long opinionId,
                                         @RequestBody OpinionForm form
    ) {
        return recordService.updateOpinion(token, opinionId, form);
    }

    //소견서 삭제하기
    @PreAuthorize("hasRole('DOCTOR')")
    @DeleteMapping("/delete-opinion")
    public String deleteOpinion(@RequestHeader(name = "TOKEN") String token,
                                @RequestParam(name = "opinion-id") Long opinionId
    ) {
        recordService.deleteOpinion(token, opinionId);
        return "소견서 삭제 완료";
    }
}
