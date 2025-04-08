package com.example.arrowdb.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PerspectiveObjectENUM {

    IN_PROGRESS ("В работе"),
    ENDED ("Завершено");

    private final String title;

}
