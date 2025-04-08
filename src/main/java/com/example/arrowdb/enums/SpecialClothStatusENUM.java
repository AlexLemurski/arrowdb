package com.example.arrowdb.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SpecialClothStatusENUM {

    ACTIVE ("Действующий"),
    CLOSED ("Закрыт");

    private final String title;

}
