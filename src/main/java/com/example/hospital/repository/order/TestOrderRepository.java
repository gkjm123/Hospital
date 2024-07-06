package com.example.hospital.repository.order;

import com.example.hospital.entity.order.TestOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TestOrderRepository extends JpaRepository<TestOrder, Long> {
}
