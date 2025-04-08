package com.example.arrowdb.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter @NoArgsConstructor
@Entity
@Table(name = "document_prof")
public class DocumentProfession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
    @JoinColumn(name = "profession_id")
    private Profession profession;

    public DocumentProfession(String docName, String docType, String key, Users author,
                              LocalDateTime dateAndTimeLastChange, Profession profession) {
        this.docName = docName;
        this.docType = docType;
        this.author = author;
        this.key = key;
        this.profession = profession;
        this.dateAndTimeLastChange = dateAndTimeLastChange;
    }
}
