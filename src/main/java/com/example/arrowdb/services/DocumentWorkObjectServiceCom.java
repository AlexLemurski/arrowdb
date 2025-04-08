package com.example.arrowdb.services;

import com.example.arrowdb.entity.DocumentWorkObjectCom;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.util.List;

public interface DocumentWorkObjectServiceCom {

    void saveDocument(DocumentWorkObjectCom documentWorkObjectCom);
    DocumentWorkObjectCom getDocumentById(Integer id);
    List<DocumentWorkObjectCom> getAllDocuments();
    void deleteDocumentById(Integer id);
    Resource downloadDocumentByKey(String key) throws IOException;

}