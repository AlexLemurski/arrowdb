package com.example.arrowdb.entity;

import com.example.arrowdb.enums.VocationTypeENUM;
import jakarta.persistence.*;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "vocation")
public class Vocation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "voc_id")
    private Integer vocId;

    @FutureOrPresent
    @Column(name = "date_start")
    private LocalDate startOfVocation;

    @Column(name = "date_end")
    private LocalDate endOfVocation;

    @Min(1)
    @Max(1500)
    @Column(name = "days")
    private int daysOfVocation;

    @Column(name = "vocation_type")
    private VocationTypeENUM vocationTypeENUM;

    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id")
    private Employee employee;

    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JoinColumn(name = "during_employee")
    private Employee duringEmployee;

    @Override
    public String toString() {
        return String.format("с %s по %s (%s дн.)",
                startOfVocation.format(DateTimeFormatter.ofPattern("dd-MM-yyyy")),
                endOfVocation.format(DateTimeFormatter.ofPattern("dd-MM-yyyy")),
                daysOfVocation);
    }

}