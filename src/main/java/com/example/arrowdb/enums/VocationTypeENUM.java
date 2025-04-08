package com.example.arrowdb.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum VocationTypeENUM {

    WITH_PAY ("Оплачиваемый"),
    WITH_OUT_PAY ("Не оплачиваемый");

    private final String title;

}