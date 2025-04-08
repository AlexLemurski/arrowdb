package com.example.arrowdb.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter @NoArgsConstructor
@Entity
@Table(name = "document_dep")
public class DocumentDepartment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "doc_id")
    private Integer docId;

    @Size(max = 1000)
    @Column(name = "doc_name")
    private String docName;

    @Column(name = "doc_type")
    @Size(max = 1000)
    private String docType;

    @Column(name = "key")
    @Size(max = 1000)
    private String key;

    @OneToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private Users author;

    @Column(name = "date_time")
    private LocalDateTime dateAndTimeLastChange;

    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id")
    private Department department;

    public DocumentDepartment(String docName, String docType, String key, Users author,
                              LocalDateTime dateAndTimeLastChange, Department department) {
        this.docName = docName;
        this.docType = docType;
        this.author = author;
        this.key = key;
        this.department = department;
        this.dateAndTimeLastChange = dateAndTimeLastChange;
    }
}
