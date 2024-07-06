package com.example.hospital.repository.order;

import com.example.hospital.entity.order.BaseOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BaseOrderRepository extends JpaRepository<BaseOrder, Long> {
    List<BaseOrder> findAllByRegist_IdOrderByOrderStartTimeDesc(Long registId);
}