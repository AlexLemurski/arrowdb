package com.example.arrowdb.services;

import com.example.arrowdb.entity.DocumentProfession;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.util.List;

public interface DocumentProfessionService {

    void saveDocument(DocumentProfession documentProfession);
    DocumentProfession getDocumentById(Integer id);
    List<DocumentProfession> getAllDocuments();
    void deleteDocumentById(Integer id);
    Resource downloadDocumentByKey(String key) throws IOException;

}