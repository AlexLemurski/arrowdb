package com.example.arrowdb.services;

import com.example.arrowdb.entity.DocumentDepartment;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.util.List;

public interface DocumentDepartmentService {

    void saveDocument(DocumentDepartment documentDepartment);
    DocumentDepartment getDocumentById(Integer id);
    List<DocumentDepartment> getAllDocuments();
    void deleteDocumentById(Integer id);
    Resource downloadDocumentByKey(String key) throws IOException;

}