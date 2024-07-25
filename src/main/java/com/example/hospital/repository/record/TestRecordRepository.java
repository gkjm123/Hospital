package com.example.hospital.repository.record;

import com.example.hospital.entity.record.TestRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TestRecordRepository extends JpaRepository<TestRecord, Long> {

  List<TestRecord> findAllByTestOrder_Id(Long testOrderId);
}
