package com.example.arrowdb.entity;

import com.example.arrowdb.enums.TechnicalConditionENUM;
import com.example.arrowdb.enums.WorkConditionENUM;
import jakarta.persistence.*;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.envers.AuditTable;
import org.hibernate.envers.Audited;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "w_instruments_cond")
@AuditTable(value = "w_instruments_aud_cond", schema = "history")
public class WorkInstrumentCondition {

    @Id
    @Column(name = "instr_id")
    private Integer workInstrId;

    @Audited
    @Column(name = "t_status")
    private TechnicalConditionENUM technicalConditionENUM;

    @Audited
    @Column(name = "w_status")
    private WorkConditionENUM workConditionENUM;

    @Audited
    @PastOrPresent
    @Column(name = "broken_date")
    private LocalDate brokenDate;

    @Audited
    @PastOrPresent
    @Column(name = "start_rep_date")
    private LocalDate startRepairDate;

    @Audited
    @FutureOrPresent
    @Column(name = "end_rep_date")
    private LocalDate endRepairDate;

    @Audited
    @Column(name = "issue_date")
    private LocalDate issueDate;

    @Audited
    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JoinColumn(name = "employee")
    private Employee employee;

    @Audited
    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JoinColumn(name = "work_object")
    private WorkObject workObject;


}