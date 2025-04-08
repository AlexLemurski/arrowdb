package com.example.arrowdb.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "document_w_object_com")
public class DocumentWorkObjectCom {

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

    @Column(name = "index")
    private int index;

    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JoinColumn(name = "work_object_id")
    private WorkObject workObject;

    public DocumentWorkObjectCom(String docName, String docType, String key, Users author,
                                 LocalDateTime dateAndTimeLastChange, WorkObject workObject) {
        this.docName = docName;
        this.docType = docType;
        this.author = author;
        this.key = key;
        this.dateAndTimeLastChange = dateAndTimeLastChange;
        this.workObject = workObject;
    }

}