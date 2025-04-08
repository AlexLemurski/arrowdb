package com.example.arrowdb.entity;

import com.example.arrowdb.enums.EmployeeStatusENUM;
import com.example.arrowdb.enums.ProfAndDepStatusENUM;
import com.example.arrowdb.enums.QualityENUM;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
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
@Table(name = "profession")
@AuditTable(value = "profession_aud", schema = "history")
public class Profession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "prof_id")
    private Integer profId;

    @Audited
    @Column(name = "prof_status")
    private ProfAndDepStatusENUM profAndDepStatusENUM;

    @Audited
    @NotBlank(message = "Поле не может быть пустым")
    @Size(min = 2, max = 100, message = "Кол-во символов от 2 до 100")
    @Pattern(regexp = "[а-яА-Я-\\s]+", message = "только - алфавит: Кириллица")
    @Column(name = "profession_name", unique = true)
    private String professionName;

    @Audited
    @Column(name = "quality")
    private QualityENUM qualityENUM;

    @Audited
    @Size(max = 1000, message = "Кол-во символов максимум 1000")
    @Pattern(regexp = "([<>|/-_.,;:«»'()#\"{}№\\n\\-\\dа-яА-Я-a-zA-Z\\s]+)?",
            message = "только - алфавит: Кириллица, Латинский; цифры; символы: <>|/-_.,;:«»'()#\"{}№")
    @Column(name = "speciality")
    private String speciality;

    @Audited
    @Min(1)
    @Max(30)
    @Column(name = "experience")
    private Integer experience;

    @Audited
    @Size(max = 1000, message = "Кол-во символов максимум 1000")
    @Pattern(regexp = "([<>|/-_.,;:«»'()#\"{}№\\n\\-\\dа-яА-Я-a-zA-Z\\s]+)?",
            message = "только - алфавит: Кириллица, Латинский; цифры; символы: <>|/-_.,;:«»'()#\"{}№")
    @Column(name = "add_requirements")
    private String additionalRequirements;

    @Audited
    @Min(0)
    @Max(100)
    @Column(name = "num_of_vacancy")
    private int numOfVacancy;

    @Audited
    @Pattern(regexp = "([,\\d\\s]+)?", message = "Допускаются только цифры")
    @Column(name = "base_salary")
    private String baseSalary;

    @Transient
    private String suffix;

    @Audited
    @PastOrPresent
    @Column(name = "close_date")
    private LocalDate closeDate;

    @OneToMany(mappedBy = "profession", cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    private List<Employee> employeeList = new ArrayList<>();

    @Transient
    private List<Employee> employeeActiveList = new ArrayList<>();

    @Transient
    private List<Employee> employeeCloseList = new ArrayList<>();

    @OneToMany(mappedBy = "profession", cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    private Set<DocumentProfession> documentProfessionList = new HashSet<>();

    public void setProfessionName(String professionName) {
        try {
            this.professionName = professionName.substring(0, 1).toUpperCase() + professionName.substring(1).trim();
        } catch (Exception e) {
            e.getStackTrace();
        }
    }

    public void setSpeciality(String speciality) {
        if (speciality.isEmpty()) {
            this.speciality = null;
        } else {
            this.speciality = speciality.trim();
        }
    }

    public void setAdditionalRequirements(String additionalRequirements) {
        if (additionalRequirements.isEmpty()) {
            this.additionalRequirements = null;
        } else {
            this.additionalRequirements = additionalRequirements.trim();
        }
    }

    public void setBaseSalary(String baseSalary) {
        if (baseSalary.isEmpty()) {
            this.baseSalary = null;
        } else {
            this.baseSalary = baseSalary.trim();
        }
    }

    public List<Employee> getEmployeeActiveList() {
        return this.employeeList.stream()
                .filter(e -> e.getEmployeeStatusENUM().equals(EmployeeStatusENUM.ACTIVE))
                .toList();
    }

    public List<Employee> getEmployeeCloseList() {
        return this.employeeList.stream()
                .filter(e -> e.getEmployeeStatusENUM().equals(EmployeeStatusENUM.CLOSED))
                .toList();
    }

    public String getSuffix() {
        if (experience == null) {
            return null;
        } else {
            int year = experience;
            String old = "";
            if (year == 1) old = "год";
            else if (year >= 2 && year <= 4) old = "года";
            else if (year >= 5 && year < 20) old = "лет";
            else if (year == 21) old = "год";
            else if (year >= 22 && year <= 24) old = "года";
            else if (year >= 25 && year < 30) old = "лет";
            return String.format(" %s", old);
        }
    }

    @Override
    public String toString() {
        return professionName;
    }

}