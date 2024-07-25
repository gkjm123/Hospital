package com.example.hospital.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class ExceptionController {

  @ExceptionHandler(CustomException.class)
  public ResponseEntity<String> exceptionHandler(final CustomException e) {
    log.warn("예외 발생: {}", e.getMessage());
    return ResponseEntity.badRequest().body(e.getMessage());
  }
}