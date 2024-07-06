package com.example.hospital.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    ID_EXIST("존재하는 아이디입니다."),
    ID_PASSWORD_INVALID("아이디,패스워드를 확인해주세요."),

    REGIST_EXIST("진행중인 접수건이 있습니다."),
    REGIST_NOT_FOUND("접수건을 찾을수 없습니다."),
    REGIST_STATUS_NOT_PRESENT("입원 진행중인 접수건이 아닙니다."),

    DOCTOR_NOT_FOUND("의사를 찾을수 없습니다."),
    DOCTOR_NOT_MATCH(" 담당 의사가 아닙니다."),

    PATIENT_NOT_FOUND("환자를 찾을수 없습니다."),
    PATIENT_NOT_MATCH("해당 환자가 아닙니다."),

    ORDER_NOT_FOUND("처방을 찾을수 없습니다."),
    ORDER_COMPLETED("완료된 처방입니다."),

    RECORD_NOT_FOUND("기록을 찾을수 없습니다."),

    LIST_EMPTY("목록이 비어있습니다.");

    private final String message;
}
