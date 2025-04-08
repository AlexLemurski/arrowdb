package com.example.arrowdb.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter @NoArgsConstructor
@Entity
@Table(name = "document_comm")
public class DocumentCommunity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer docId;

    @Size(max = 1000)
    @Column(name = "doc_name")
    private String docName;

    @Column(name = "doc_type")
    @Size(max = 1000)
    private String docType;

    @Column(name = "size")
    @Size(max = 1000)
    private String size;

    @Column(name = "key")
    @Size(max = 1000)
    private String key;

    @OneToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private Users author;

    @Column(name = "date_time")
    private LocalDateTime dateAndTimeLastChange;

    public DocumentCommunity(String docName, String docType, String size, String key, Users author,
                             LocalDateTime dateAndTimeLastChange) {
        this.docName = docName;
        this.docType = docType;
        this.author = author;
        this.size = size;
        this.key = key;
        this.dateAndTimeLastChange = dateAndTimeLastChange;
    }
}