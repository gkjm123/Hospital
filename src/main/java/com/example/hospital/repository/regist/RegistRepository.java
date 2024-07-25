package com.example.hospital.repository.regist;

import com.example.hospital.entity.regist.Regist;
import com.example.hospital.type.RegistType;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RegistRepository extends JpaRepository<Regist, Long> {

  Optional<Regist> findByPatient_IdAndRegistType(Long patientId, RegistType registType);

  boolean existsByPatient_IdAndRegistType(Long patientId, RegistType registType);

  Page<Regist> findAllByDoctor_IdAndRegistType(Long doctorId, RegistType registType,
      Pageable pageable);
}
