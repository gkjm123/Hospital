package com.example.hospital.repository.member;

import com.example.hospital.entity.member.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long> {
    Optional<Doctor> findByLoginId(String loginId);
    boolean existsByLoginId(String loginId);
}
