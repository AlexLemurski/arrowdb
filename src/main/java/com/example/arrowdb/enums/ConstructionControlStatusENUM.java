package com.example.arrowdb.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ConstructionControlStatusENUM {

    DRAFT ("Черновик"),
    ACTIVE ("Действующий"),
    CLOSED ("Закрыт");

    private final String title;
}
