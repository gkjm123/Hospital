package com.example.hospital.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TakeType {
    QD("하루 한번(아침) 복용", 1L),
    BID("하루 두번(아침,저녁) 복용", 2L),
    TID("하루 세번(아침,점심,저녁) 복용", 3L),
    DPE("하루 한번(저녁) 복용", 1L),
    HS("하루 한번(자기전) 복용", 1L);

    private final String description;
    private final Long time;
}
