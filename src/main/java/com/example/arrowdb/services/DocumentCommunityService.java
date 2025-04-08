package com.example.arrowdb.services;

import com.example.arrowdb.entity.DocumentCommunity;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface DocumentCommunityService {

    void saveDocument(MultipartFile file);
    DocumentCommunity getDocumentById(Integer id);
    List<DocumentCommunity> getAllDocuments();
    void deleteDocumentById(Integer id);
    Resource downloadDocumentByKey(String key) throws IOException;

}