package com.example.arrowdb.entity;

import com.example.arrowdb.enums.PerspectiveObjectENUM;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "perspective_object")
public class PerspectiveObject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "perspective_id")
    private Integer perspectiveId;

    @NotBlank
    @Size(min = 2, max = 100, message = "Кол-во символов от 2 до 100")
    @Column(name = "perspective_lot", unique = true, nullable = false)
    private String perspectiveLot;

    @NotBlank
    @Size(min = 2, max = 1000, message = "Кол-во символов от 2 до 100")
    @Column(name = "perspective_name", unique = true, nullable = false)
    private String perspectiveName;

    @NotBlank
    @Size(min = 2, max = 100, message = "Кол-во символов от 2 до 100")
    @Column(name = "gen_contractor", nullable = false)
    private String generalContractor;

    @NotBlank
    @Size(min = 2, max = 1000, message = "Кол-во символов от 2 до 100")
    @Column(name = "target", nullable = false)
    private String target;

    @Column(name = "date_of_dead_line")
    private LocalDate dateOfDeadline;

    @Column(name = "date_time_of_end")
    private LocalDateTime dateOfEnd;

    @Column(name = "file_path")
    @Size(max = 1000)
    private String filePath;

    @Column(name = "status")
    private PerspectiveObjectENUM perspectiveObjectENUM;

    @OneToMany(mappedBy = "perspectiveObject", cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    List<DocumentPerspective> documentPerspectiveList = new ArrayList<>();

    @ManyToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JoinTable(name = "emp_equop_perspect_obj_join",
            joinColumns = @JoinColumn(name = "join_p_obj_id"),
            inverseJoinColumns = @JoinColumn(name = "join_emp_id"))
    private Set<Employee> employeeEquipmentList = new HashSet<>();

    @ManyToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JoinTable(name = "emp_work_perspect_obj_join",
            joinColumns = @JoinColumn(name = "join_p_obj_id"),
            inverseJoinColumns = @JoinColumn(name = "join_emp_id"))
    private Set<Employee> employeeWorkList = new HashSet<>();

    @ManyToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JoinTable(name = "emp_observer_perspect_obj_join",
            joinColumns = @JoinColumn(name = "join_p_obj_id"),
            inverseJoinColumns = @JoinColumn(name = "join_emp_id"))
    private Set<Employee> employeeObserverList = new HashSet<>();

    public void setFilePath(@NotNull String filePath) {
        if (filePath.isEmpty()) {
            this.filePath = null;
        } else {
            this.filePath = filePath;
        }
    }

    @Override
    public String toString() {
        return perspectiveLot;
    }

}