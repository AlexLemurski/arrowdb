package com.example.arrowdb.entity;

import com.example.arrowdb.enums.ClothSizeENUM;
import com.example.arrowdb.enums.DriverLicenseENUM;
import com.example.arrowdb.enums.EmployeeStatusENUM;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.envers.AuditTable;
import org.hibernate.envers.Audited;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.Period;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "employee")
@AuditTable(value = "employee_aud", schema = "history")
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "emp_id")
    @OrderBy
    private Integer empId;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "account")
    private Users account;

    @Audited
    @NotBlank(message = "Поле не может быть пустым")
    @Size(min = 2, max = 45, message = "Кол-во символов от 2 до 45")
    @Pattern(regexp = "^[а-яА-ЯёЁ]+$", message = "только - алфавит: Кириллица")
    @Column(name = "sur_name")
    private String surName;

    @Audited
    @NotBlank(message = "Поле не может быть пустым")
    @Size(min = 2, max = 45, message = "Кол-во символов от 2 до 45")
    @Pattern(regexp = "^[а-яА-ЯёЁ]+$", message = "только - алфавит: Кириллица")
    @Column(name = "name")
    private String name;

    @Audited
    @NotBlank(message = "Поле не может быть пустым")
    @Size(min = 2, max = 45, message = "Кол-во символов от 2 до 45")
    @Pattern(regexp = "^[а-яА-ЯёЁ]+$", message = "только - алфавит: Кириллица")
    @Column(name = "middle_name")
    private String middleName;

    @Audited
    @Pattern(regexp = "[+7]*[(]*[0-9]*[)]*[0-9]*-*[0-9]*-*[0-9]*",
            message = "Номер телефона должен состоять из 16 знаков в формате '+7(000)000-00-00'")
    @Column(name = "phone_number")
    private String phoneNumber;

    @Audited
    @Email(regexp = "^\\S+@\\S+\\.\\S+$",
            message = "только - формат ввода: name@example.com")
    @Column(name = "email")
    private String email;

    @Audited
    @PastOrPresent
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    @Column(name = "hire_date")
    private LocalDate hireDate;

    @PastOrPresent
    @Column(name = "close_date")
    private LocalDate closeDate;

    @Transient
    private String timeExperience;

    @Audited
    @Min(100)
    @Max(300)
    @Column(name = "height")
    private Integer height;

    @Audited
    @Column(name = "cloth_size")
    private ClothSizeENUM clothSizeENUM;

    @Audited
    @Min(15)
    @Max(60)
    @Column(name = "shoes_size")
    private Integer shoesSize;

    @Pattern(regexp = "[+7]*[(]*[0-9]*[)]*[0-9]*-*[0-9]*-*[0-9]*",
            message = "Номер телефона должен состоять из 16 знаков в формате '+7(000)000-00-00'")
    @Column(name = "phone_number_pers")
    private String phoneNumberPersonal;

    @Email(regexp = "^\\S+@\\S+\\.\\S+$",
            message = "только - формат ввода: name@example.com")
    @Column(name = "email_pers")
    private String emailPersonal;

    @Size(max = 1000, message = "Кол-во символов максимум 1000")
    @Pattern(regexp = "([<>|/-_.,;:«»'()#\"{}№\\n\\-\\dа-яА-Я-a-zA-Z\\s]+)?",
            message = "только - алфавит: Кириллица, Латинский; цифры; символы: <>|/-_.,;:«»'()#\"{}№")
    @Column(name = "address")
    private String address;

    @PastOrPresent
    @Column(name = "birth_date")
    private LocalDate birthDate;

    @Transient
    private int age;

    @Pattern(regexp = "([,.\\d\\s]+)?", message = "Допускаются только цифрыи запятые")
    @Column(name = "base_salary")
    private String baseSalary;

    @Min(0)
    @Max(999)
    @Column(name = "percent_of_bonus")
    private Integer percentOfBonus;

    @Audited
    @Column(name = "employee_status")
    private EmployeeStatusENUM employeeStatusENUM;

    @Audited
    @Column(name = "driver_license")
    private List<DriverLicenseENUM> driverLicenseENUM;

    @Audited
    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JoinColumn(name = "profession_id")
    private Profession profession;

    @Audited
    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id")
    private Department department;

    @OneToMany(cascade = CascadeType.REFRESH, mappedBy = "employee", fetch = FetchType.LAZY)
    private Set<PersonalInstrument> personalInstrumentList = new HashSet<>();

    @OneToMany(cascade = CascadeType.REFRESH, mappedBy = "employee", fetch = FetchType.LAZY)
    private Set<WorkInstrument> workInstrumentList = new HashSet<>();

    @OneToMany(cascade = CascadeType.REFRESH, mappedBy = "employee", fetch = FetchType.LAZY)
    private Set<MeasInstrument> measInstrumentList = new HashSet<>();

    @OneToMany(cascade = CascadeType.REFRESH, mappedBy = "employee", fetch = FetchType.LAZY)
    private Set<SpecialClothEmployee> specialClothEmployeeList = new HashSet<>();

    @OneToMany(cascade = CascadeType.REFRESH, mappedBy = "workObjectChief", fetch = FetchType.LAZY)
    private Set<WorkObject> workObjectChiefList = new HashSet<>();

    @OneToMany(cascade = CascadeType.REFRESH, mappedBy = "responsibleFromContractor", fetch = FetchType.LAZY)
    private Set<ConstructionControl> responsibleFromContractorList = new HashSet<>();

    @OneToMany(cascade = CascadeType.REFRESH, mappedBy = "responsibleFromSKContractor", fetch = FetchType.LAZY)
    private Set<ConstructionControl> responsibleFromSKContractorList = new HashSet<>();

    @ManyToMany(mappedBy = "storeKeeperList", cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    private Set<WorkObject> workObjectStoreKeeperList = new HashSet<>();

    @ManyToMany(mappedBy = "supplierList", cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    private Set<WorkObject> workObjectSupplierList = new HashSet<>();

    @ManyToMany(mappedBy = "PTOList", cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    private Set<WorkObject> workObjectPTOList = new HashSet<>();

    @ManyToMany(mappedBy = "employeeEquipmentList", cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    private Set<PerspectiveObject> employeeEquipmentList = new HashSet<>();

    @ManyToMany(mappedBy = "employeeWorkList", cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    private Set<PerspectiveObject> employeeWorkList = new HashSet<>();

    @ManyToMany(mappedBy = "employeeObserverList", cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    private Set<PerspectiveObject> employeeObserverList = new HashSet<>();

    @OneToMany(mappedBy = "employee", cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    private Set<Vocation> vocationList = new HashSet<>();

    public void setSurName(String surName) {
        try {
            this.surName = surName.substring(0, 1).toUpperCase() + surName.substring(1).trim();
        } catch (Exception e) {
            e.getStackTrace();
        }
    }

    public void setName(String name) {
        try {
            this.name = name.substring(0, 1).toUpperCase() + name.substring(1).trim();
        } catch (Exception e) {
            e.getStackTrace();
        }
    }

    public void setMiddleName(String middleName) {
        try {
            this.middleName = middleName.substring(0, 1).toUpperCase() + middleName.substring(1).trim();
        } catch (Exception e) {
            e.getStackTrace();
        }
    }

    public void setPhoneNumber(String phoneNumber) {
        if (phoneNumber.equals("+7(___)___-__-__") || phoneNumber.isEmpty()) {
            this.phoneNumber = null;
        } else {
            this.phoneNumber = phoneNumber;
        }
    }

    public void setEmail(String email) {
        if (email.isEmpty()) {
            this.email = null;
        } else {
            this.email = email.trim();
        }
    }

    public void setPhoneNumberPersonal(String phoneNumberPersonal) {
        if (phoneNumberPersonal.isEmpty()) {
            this.phoneNumberPersonal = null;
        } else {
            this.phoneNumberPersonal = phoneNumberPersonal.trim();
        }
    }

    public void setEmailPersonal(String emailPersonal) {
        if (emailPersonal.isEmpty()) {
            this.emailPersonal = null;
        } else {
            this.emailPersonal = emailPersonal.trim();
        }
    }

    public void setAddress(String address) {
        if (address.isEmpty()) {
            this.address = null;
        } else {
            this.address = address.trim();
        }
    }

    public void setBaseSalary(String baseSalary) {
        if (baseSalary.isEmpty()) {
            this.baseSalary = null;
        } else {
            this.baseSalary = baseSalary.trim();
        }
    }

    public void setDriverLicenseENUM(List<DriverLicenseENUM> driverLicenseENUM) {
        if (driverLicenseENUM.isEmpty()) {
            this.driverLicenseENUM = null;
        } else {
            this.driverLicenseENUM = driverLicenseENUM;
        }
    }

    public String getTimeExperience() {
        if (getHireDate() == null) {
            return null;
        } else {
            if (employeeStatusENUM.getTitle().equals("Закрыт") && closeDate != null) {
                LocalDate hireDate = getHireDate();
                Period period = hireDate.until(closeDate);
                int year = period.getYears();
                int month = period.getMonths();
                int day = period.getDays();
                String old = "";
                if (year == 0) old = "лет";
                else if (year == 1) old = "год";
                else if (year >= 2 && year <= 4) old = "года";
                else if (year >= 5 && year < 20) old = "лет";
                else if (year == 21) old = "год";
                else if (year >= 22 && year <= 24) old = "года";
                else if (year >= 25 && year < 30) old = "лет";
                return String.format(" (Стаж составил: %d %s %d мес %d дн)", year, old, month, day);
            } else {
                LocalDate hireDate = getHireDate();
                Period period = hireDate.until(LocalDate.now());
                int year = period.getYears();
                int month = period.getMonths();
                int day = period.getDays();
                String old = "";
                if (year == 0) old = "лет";
                else if (year == 1) old = "год";
                else if (year >= 2 && year <= 4) old = "года";
                else if (year >= 5 && year < 20) old = "лет";
                else if (year == 21) old = "год";
                else if (year >= 22 && year <= 24) old = "года";
                else if (year >= 25 && year < 30) old = "лет";
                return String.format(" (Стаж составляет: %d %s %d мес %d дн)", year, old, month, day);
            }
        }
    }

    public int getAge() {
        return LocalDate.now().getYear() - getBirthDate().getYear();
    }

    public List<Vocation> getVocationList() {
        return vocationList.stream()
                .sorted(Comparator.comparing(Vocation::getStartOfVocation))
                .toList();
    }

    @Override
    public String toString() {
        return String.format("%s %s.%s.", surName, name.charAt(0), middleName.charAt(0));
    }
}