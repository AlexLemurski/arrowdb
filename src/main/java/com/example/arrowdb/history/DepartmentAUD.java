package com.example.arrowdb.history;

import com.example.arrowdb.entity.Employee;
import com.example.arrowdb.enums.ProfAndDepStatusENUM;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor
@Entity
@Table(name = "department_aud", schema = "history")
public class DepartmentAUD {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private Integer id;

    @Column(name = "dep_id")
    private Integer depId;

    @Column(name = "dep_status")
    private ProfAndDepStatusENUM profAndDepStatusENUM;

    @Column(name = "dep_name")
    private String depName;

    @Size(max = 1000)
    @Column(name = "dep_activity")
    private String depActivity;

    @Size(max = 1000)
    @Column(name = "dep_target")
    private String depTarget;

    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JoinColumn(name = "dep_chief")
    private Employee departmentChief;

    @OneToOne
    @JoinColumn(name = "rev")
    private ExampleRevEntity exampleRevEntity;

}