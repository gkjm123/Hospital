package com.example.hospital.entity.record;

import com.example.hospital.entity.regist.Regist;
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
public class Opinion {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  private Regist regist;

  //소견
  private String opinion;

  @CreationTimestamp
  private LocalDateTime created;
}
