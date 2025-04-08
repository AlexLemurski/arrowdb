package com.example.arrowdb.history;

import com.example.arrowdb.entity.Employee;
import com.example.arrowdb.entity.WorkObject;
import com.example.arrowdb.enums.TechnicalConditionENUM;
import com.example.arrowdb.enums.WorkConditionENUM;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "w_instruments_aud_cond", schema = "history")
public class WorkInstrumentConditionAUD {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private Integer id;

    @Column(name = "instr_id")
    private Integer workInstrId;

    @Column(name = "t_status")
    private TechnicalConditionENUM technicalConditionENUM;

    @Column(name = "w_status")
    private WorkConditionENUM workConditionENUM;

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

    @ManyToOne
    @JoinColumn(name = "work_object")
    private WorkObject workObject;

    @OneToOne
    @JoinColumn(name = "rev")
    private ExampleRevEntity exampleRevEntity;

}