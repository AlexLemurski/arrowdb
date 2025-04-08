package com.example.arrowdb.services;

import com.example.arrowdb.entity.DocumentWorkObject;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.util.List;

public interface DocumentWorkObjectService {

    void saveDocument(DocumentWorkObject documentWorkObject);
    DocumentWorkObject getDocumentById(Integer id);
    List<DocumentWorkObject> getAllDocuments();
    void deleteDocumentById(Integer id);
    Resource downloadDocumentByKey(String key) throws IOException;

    List<DocumentWorkObject> findAllByIndex(int index, int id);

}
