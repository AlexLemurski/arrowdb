package com.example.arrowdb.services;

import com.example.arrowdb.entity.DocumentWorkObject;
import com.example.arrowdb.repositories.DocumentWorkObjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

import static com.example.arrowdb.auxiliary.FilePathResource.FILES_DIRECTORY;

@Service
@RequiredArgsConstructor
public class DocumentWorkObjectServiceImpl implements DocumentWorkObjectService{

    private final DocumentWorkObjectRepository documentWorkObjectRepository;

    @Override
    @Transactional(rollbackFor = {IOException.class})
    public void saveDocument(DocumentWorkObject documentPerspective) {
        documentWorkObjectRepository.save(documentPerspective);
    }

    @Override
    @Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED)
    public DocumentWorkObject getDocumentById(Integer id) {
        DocumentWorkObject documentWorkObject = null;
        Optional<DocumentWorkObject> optionalDocument = documentWorkObjectRepository.findById(id);
        if (optionalDocument.isPresent()) {
            documentWorkObject = optionalDocument.get();
        }
        return documentWorkObject;
    }

    @Override
    @Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED)
    public List<DocumentWorkObject> getAllDocuments() {
        return documentWorkObjectRepository.findAll();
    }

    @Override
    @Transactional(rollbackFor = {IOException.class})
    public void deleteDocumentById(Integer id) {
        DocumentWorkObject documentWorkObject = getDocumentById(id);
        Path path = Paths.get(new File(FILES_DIRECTORY) + "/" + documentWorkObject.getKey());
        try {
            Files.delete(path);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        documentWorkObjectRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED)
    public Resource downloadDocumentByKey(String key) throws IOException {
        Path path = Paths.get(new File(FILES_DIRECTORY) + "/" + key);
        Resource resource = new UrlResource(path.toUri());
        if (resource.exists() || resource.isReadable()) {
            return resource;
        } else {
            throw new IOException();
        }
    }

    @Override
    @Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED)
    public List<DocumentWorkObject> findAllByIndex(int index, int id) {
        return documentWorkObjectRepository.findAllByIndex(index, id);
    }
}
