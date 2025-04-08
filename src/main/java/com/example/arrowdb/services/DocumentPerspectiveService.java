package com.example.arrowdb.services;

import com.example.arrowdb.entity.DocumentPerspective;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.util.List;

public interface DocumentPerspectiveService {

    void saveDocument(DocumentPerspective documentPerspective);
    DocumentPerspective getDocumentById(Integer id);
    List<DocumentPerspective> getAllDocuments();
    void deleteDocumentById(Integer id);
    Resource downloadDocumentByKey(String key) throws IOException;

    List<DocumentPerspective> findAllByIndex(int index, int id);

}