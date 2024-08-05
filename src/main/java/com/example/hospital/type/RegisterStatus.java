package com.example.hospital.type;

public enum RegisterStatus {
  REGISTERED, //입원 등록
  WAIT_FOR_PAY, //정산 대기중
  DISCHARGE //퇴원 완료
}