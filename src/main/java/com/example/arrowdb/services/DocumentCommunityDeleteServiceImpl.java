package com.example.arrowdb.services;

import com.example.arrowdb.entity.DocumentCommunityDeleted;
import com.example.arrowdb.repositories.DocumentCommunityDeleteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@RequiredArgsConstructor
public class DocumentCommunityDeleteServiceImpl implements DocumentCommunityDeleteService {

    private final DocumentCommunityDeleteRepository documentCommunityDeleteRepository;

    @Override
    @Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED)
    public List<DocumentCommunityDeleted> getAllDeleteDocuments() {
        return documentCommunityDeleteRepository.findAllDeleteDocuments();
    }

    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void saveDocument(DocumentCommunityDeleted documentCommunityDeleted) {
        documentCommunityDeleteRepository.save(documentCommunityDeleted);
    }
}