package com.example.hospital.entity.record;

import com.example.hospital.entity.order.TestOrder;
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
public class TestRecord {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  private TestOrder testOrder;

  //검사결과
  private String result;

  //처방일
  @CreationTimestamp
  private LocalDateTime orderedDate;

  //검사일
  private LocalDateTime testDate;
}
