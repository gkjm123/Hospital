package com.example.hospital.repository.order;

import com.example.hospital.entity.order.BaseOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BaseOrderRepository extends JpaRepository<BaseOrder, Long> {
    Page<BaseOrder> findAllByRegist_Id(Long registId, Pageable pageable);
}