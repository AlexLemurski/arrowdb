package com.example.arrowdb.auxiliary;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@Component
public class TempIssueDate {

    private LocalDate tIssueDate;

}