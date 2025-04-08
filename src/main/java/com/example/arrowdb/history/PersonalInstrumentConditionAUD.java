package com.example.arrowdb.history;

import com.example.arrowdb.entity.Employee;
import com.example.arrowdb.enums.PersonalConditionENUM;
import com.example.arrowdb.enums.TechnicalConditionENUM;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "p_instruments_aud_cond", schema = "history")
public class PersonalInstrumentConditionAUD {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private Integer id;

    @Column(name = "instr_id")
    private Integer personalInstrId;

    @Column(name = "t_status")
    private TechnicalConditionENUM technicalConditionENUM;

    @Column(name = "p_status")
    private PersonalConditionENUM personalConditionENUM;

    @Column(name = "start_rep_date")
    private LocalDate startRepDate;

    @Column(name = "end_rep_date")
    private LocalDate endRepDate;

    @Column(name = "broken_date")
    private LocalDate brokenDate;

    @Column(name = "issue_date")
    private LocalDate issueDate;

    @ManyToOne
    @JoinColumn(name = "employee")
    private Employee employee;

    @OneToOne
    @JoinColumn(name = "rev")
    private ExampleRevEntity exampleRevEntity;

}