package com.example.hospital.repository.regist;

import com.example.hospital.entity.regist.Regist;
import com.example.hospital.type.RegistType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RegistRepository extends JpaRepository<Regist, Long> {
    Optional<Regist> findByPatient_IdAndRegistType(Long patientId, RegistType registType);
    boolean existsByPatient_IdAndRegistType(Long patientId, RegistType registType);
    List<Regist> findAllByDoctor_IdAndRegistType(Long doctorId, RegistType registType);
}
