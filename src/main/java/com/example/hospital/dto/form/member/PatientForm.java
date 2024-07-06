package com.example.hospital.dto.form.member;

import com.example.hospital.type.SexType;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PatientForm {
    private String loginId;
    private String password;
    private String name;
    private SexType sex;
    private Long age;
    private String phone;
    private String address;
}
