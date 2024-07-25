package com.example.hospital.dto.form.member;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DoctorForm {

  private String loginId;
  private String password;
  private String name;
  private String phone;
  private String major;
}
