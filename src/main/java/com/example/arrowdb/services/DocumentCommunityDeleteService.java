package com.example.arrowdb.services;

import com.example.arrowdb.entity.DocumentCommunityDeleted;

import java.util.List;

public interface DocumentCommunityDeleteService {

    List<DocumentCommunityDeleted> getAllDeleteDocuments();
    void saveDocument(DocumentCommunityDeleted documentCommunityDeleted);

}