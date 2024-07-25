package com.example.hospital.repository.order;

import com.example.hospital.entity.order.MedicineOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MedicineOrderRepository extends JpaRepository<MedicineOrder, Long> {

}
