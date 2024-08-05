package com.example.hospital.entity.member;

import com.example.hospital.type.Role;
import com.example.hospital.type.Gender;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Patient implements UserDetails {

  @Id
  @GeneratedValue
  private Long id;

  private String loginId;
  private String password;
  private String name;

  @Enumerated(EnumType.STRING)
  private Gender sex;

  private Long age;
  private String phone;
  private String address;
  private Role role;

  @CreationTimestamp
  private LocalDateTime createdAt;

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return List.of(new SimpleGrantedAuthority(role.toString()));
  }

  @Override
  public String getUsername() {
    return "";
  }
}
