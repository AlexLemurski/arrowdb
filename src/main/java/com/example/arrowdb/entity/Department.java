package com.example.arrowdb.entity;

import com.example.arrowdb.enums.EmployeeStatusENUM;
import com.example.arrowdb.enums.ProfAndDepStatusENUM;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.BatchSize;
import org.hibernate.envers.AuditTable;
import org.hibernate.envers.Audited;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "department")
@AuditTable(value = "department_aud", schema = "history")
public class Department {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "dep_id")
    private Integer depId;

    @Audited
    @Column(name = "dep_status")
    private ProfAndDepStatusENUM profAndDepStatusENUM;

    @Audited
    @NotBlank(message = "Поле не может быть пустым")
    @Size(min = 2, max = 100, message = "Кол-во символов от 2 до 100")
    @Pattern(regexp = "[а-яА-Я-\\s]+", message = "только - алфавит: Кириллица")
    @Column(name = "dep_name", unique = true)
    private String depName;

    @Audited
    @Size(max = 1000, message = "Кол-во символов максимум 1000")
    @Pattern(regexp = "([<>|/-_.,;:«»'()#\"{}№\\n\\-\\dа-яА-Я-a-zA-Z\\s]+)?",
            message = "только - алфавит: Кириллица, Латинский; цифры; символы: <>|/-_.,;:«»'()#\"{}№")
    @Column(name = "dep_activity")
    private String depActivity;

    @Audited
    @Size(max = 1000, message = "Кол-во символов максимум 1000")
    @Pattern(regexp = "([<>|/-_.,;:«»'()#\"{}№\\n\\-\\dа-яА-Я-a-zA-Z\\s]+)?",
            message = "только - алфавит: Кириллица, Латинский; цифры; символы: <>|/-_.,;:«»'()#\"{}№")
    @Column(name = "dep_target")
    private String depTarget;

    @PastOrPresent
    @Column(name = "close_date")
    private LocalDate closeDate;

    @Transient
    private int countOfActiveEmployee;

    @Audited
    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JoinColumn(name = "dep_chief")
    private Employee departmentChief;

    @OneToMany(cascade = CascadeType.REFRESH, mappedBy = "department", fetch = FetchType.LAZY)
    private List<Employee> personalOfDepartment = new ArrayList<>();

    @Transient
    private List<Employee> personalActiveOfDepartment = new ArrayList<>();

    @Transient
    private List<Employee> personalCloseOfDepartment = new ArrayList<>();

    @OneToMany(cascade = CascadeType.REFRESH, mappedBy = "department", fetch = FetchType.LAZY)
    private Set<WorkInstrument> workInstrumentList = new HashSet<>();

    @OneToMany(cascade = CascadeType.REFRESH, mappedBy = "department", fetch = FetchType.LAZY)
    private Set<MeasInstrument> measInstrumentList = new HashSet<>();

    @OneToMany(mappedBy = "department", cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    private Set<DocumentDepartment> documentDepartmentList = new HashSet<>();

    public void setDepName(String depName) {
        try {
            this.depName = depName.substring(0, 1).toUpperCase() + depName.substring(1).trim();
        } catch (Exception e) {
            e.getStackTrace();
        }
    }

    public void setDepTarget(String depTarget) {
        if (depTarget.isEmpty()) {
            this.depTarget = null;
        } else {
            this.depTarget = depTarget.trim();
        }
    }

    public void setDepActivity(String depActivity) {
        if (depActivity.isEmpty()) {
            this.depActivity = null;
        } else {
            this.depActivity = depActivity.trim();
        }
    }

    public List<Employee> getPersonalActiveOfDepartment() {
        return this.personalOfDepartment.stream()
                .filter(e -> e.getEmployeeStatusENUM().equals(EmployeeStatusENUM.ACTIVE))
                .toList();
    }

    public List<Employee> getPersonalCloseOfDepartment() {
        return this.personalOfDepartment.stream()
                .filter(e -> e.getEmployeeStatusENUM().equals(EmployeeStatusENUM.CLOSED))
                .toList();
    }

    @Override
    public String toString() {
        return depName;
    }


}
