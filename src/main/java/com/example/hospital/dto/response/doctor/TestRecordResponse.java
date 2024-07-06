package com.example.hospital.dto.response.doctor;

import com.example.hospital.entity.record.TestRecord;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TestRecordResponse {
    private Long id;
    private Long testOrderId;
    private String result;
    private LocalDateTime orderedDate;
    private LocalDateTime testDate;

    public static TestRecordResponse fromEntity(TestRecord testRecord) {
        return TestRecordResponse.builder()
                .id(testRecord.getId())
                .testOrderId(testRecord.getTestOrder().getId())
                .result(testRecord.getResult())
                .orderedDate(testRecord.getOrderedDate())
                .testDate(testRecord.getTestDate())
                .build();
    }
}
