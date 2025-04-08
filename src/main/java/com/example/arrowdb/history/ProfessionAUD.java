package com.example.arrowdb.history;

import com.example.arrowdb.enums.ProfAndDepStatusENUM;
import com.example.arrowdb.enums.QualityENUM;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter @Setter @NoArgsConstructor
@Entity
@Table(name = "profession_aud", schema = "history")
public class ProfessionAUD {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private Integer id;

    @Column(name = "prof_id")
    private Integer profId;

    @Column(name = "prof_status")
    private ProfAndDepStatusENUM profAndDepStatusENUM;

    @Column(name = "profession_name")
    private String professionName;

    @Column(name = "quality")
    private QualityENUM qualityENUM;

    @Size(max = 1000)
    @Column(name = "speciality")
    private String speciality;

    @Column(name = "experience")
    private Integer experience;

    @Size(max = 1000)
    @Column(name = "add_requirements")
    private String additionalRequirements;

    @Column(name = "num_of_vacancy")
    private int numOfVacancy;

    @Column(name = "base_salary")
    private String baseSalary;

    @Column(name = "close_date")
    private LocalDate closeDate;

    @OneToOne
    @JoinColumn(name = "rev")
    private ExampleRevEntity exampleRevEntity;

}