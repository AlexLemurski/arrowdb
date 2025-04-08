package com.example.arrowdb.history;

import com.example.arrowdb.entity.Department;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "w_instruments_aud", schema = "history")
public class WorkInstrumentAUD {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private Integer Id;

    @Column(name = "instr_id")
    private Integer workInstrId;

    @Column(name = "onec_number")
    private String workOneCNumber;

    @Column(name = "invent_number")
    private String workInventNumber;

    @Column(name = "serial_number")
    private String workSerialNumber;

    @Column(name = "instr_name")
    private String workInstrName;

    @Column(name = "instr_model")
    private String workInstrModel;

    @Column(name = "instr_manufacturer")
    private String workInstrManufacturer;

    @Column(name = "date_of_purchase")
    private LocalDate workDateOfPurchase;

    @Column(name = "time_exp")
    private String workTimeExperience;

    @Column(name = "guarantee_period")
    private Integer workGuaranteePeriod;

    @Column(name = "time_of_guarantee")
    private String workTimeOfGuarantee;

    @Column(name = "service_period")
    private Integer workServicePeriod;

    @Column(name = "time_of_service")
    private String workTimeOfService;

    @Column(name = "instr_comment")
    @Size(max = 1000)
    private String workInstrComment;

    @Column(name = "work_price")
    private String workInstrumentPrice;

    @ManyToOne(cascade = CascadeType.REFRESH)
    @JoinColumn(name = "dep_status")
    private Department department;

    @OneToOne
    @JoinColumn(name = "rev")
    private ExampleRevEntity exampleRevEntity;

}