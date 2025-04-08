package com.example.arrowdb.history;

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
@Table(name = "p_instruments_aud", schema = "history")
public class PersonalInstrumentAUD {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private Integer id;

    @Column(name = "instr_id")
    private Integer personalInstrId;

    @Column(name = "onec_number")
    private String personalOneCNumber;

    @Column(name = "invent_number")
    private String personalInventNumber;

    @Column(name = "serial_number")
    private String personalSerialNumber;

    @Column(name = "instr_name")
    private String personalInstrName;

    @Column(name = "instr_model")
    private String personalInstrModel;

    @Column(name = "instr_manufacturer")
    private String personalInstrManufacturer;

    @Column(name = "date_of_purchase")
    private LocalDate personalDateOfPurchase;

    @Column(name = "time_exp")
    private String personalTimeExperience;

    @Column(name = "guarantee_period")
    private Integer personalGuaranteePeriod;

    @Column(name = "time_of_guarantee")
    private String personalTimeOfGuarantee;

    @Column(name = "service_period")
    private Integer personalServicePeriod;

    @Column(name = "time_of_service")
    private String personalTimeOfService;

    @Column(name = "instr_comment")
    @Size(max = 1000)
    private String personalInstrComment;

    @Column(name = "pers_price")
    private String personalInstrumentPrice;

    @OneToOne
    @JoinColumn(name = "rev")
    private ExampleRevEntity exampleRevEntity;

}
