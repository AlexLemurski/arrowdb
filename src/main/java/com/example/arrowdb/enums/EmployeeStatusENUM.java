package com.example.arrowdb.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum EmployeeStatusENUM {

    DRAFT("Черновик"),
    ACTIVE ("Действующий"),
    IN_BUSINESS_TRIP ("В командировке"),
    IN_VOCATION ("В отпуске"),
    ON_SICK_LEAVE ("На больничном"),
    CLOSED ("Закрыт");

    private final String title;

}