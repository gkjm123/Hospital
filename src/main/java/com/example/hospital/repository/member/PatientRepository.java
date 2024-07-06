package com.example.hospital.repository.member;

import com.example.hospital.entity.member.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {
    Optional<Patient> findByLoginId(String loginId);
    boolean existsByLoginId(String loginId);
}
