package com.example.hospital.dto.form.member;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SignInForm {

  private String loginId;
  private String password;
}
