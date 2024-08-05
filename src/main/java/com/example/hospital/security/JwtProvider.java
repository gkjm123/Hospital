package com.example.hospital.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtProvider {

  private final BCryptPasswordEncoder bCryptPasswordEncoder;

  @Value("${token.key}")
  private String key;

  public String passwordEncode(String password) {
    return bCryptPasswordEncoder.encode(password);
  }

  public boolean checkPassword(String rawPassword, String encodedPassword) {
    return bCryptPasswordEncoder.matches(rawPassword, encodedPassword);
  }

  public String createToken(String loginId) {
    Claims claims = Jwts.claims().setSubject(loginId);

    Date now = new Date();
    Date expiredDate = new Date(now.getTime() + 60 * 60 * 24 * 1000);

    return Jwts.builder().setClaims(claims)
        .setIssuedAt(now)
        .setExpiration(expiredDate)
        .signWith(SignatureAlgorithm.HS512, key)
        .compact();
  }

  public Claims parseToken(String token) {
    token = token.replace("doctor ", "");
    token = token.replace("patient ", "");
    return Jwts.parser().setSigningKey(key).parseClaimsJws(token).getBody();
  }
}
