package com.example.hospital.entity.regist;

import com.example.hospital.entity.member.Doctor;
import com.example.hospital.entity.member.Patient;
import com.example.hospital.type.RegisterStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Regist {

  @Id
  @GeneratedValue
  private Long id;

  //의사
  @ManyToOne(fetch = FetchType.LAZY)
  private Doctor doctor;

  //환자
  @ManyToOne(fetch = FetchType.LAZY)
  private Patient patient;

  //진단명
  private String diagnosis;

  //접수상태
  @Enumerated(EnumType.STRING)
  private RegisterStatus registerStatus;

  //비용
  private Long cost;

  //접수일(입원일)
  @CreationTimestamp
  private LocalDateTime registrationDate;

  //퇴원일
  private LocalDateTime dischargeDate;
}
