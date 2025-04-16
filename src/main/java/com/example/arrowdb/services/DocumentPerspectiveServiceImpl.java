package com.example.arrowdb.services;

import com.example.arrowdb.entity.DocumentPerspective;
import com.example.arrowdb.repositories.DocumentPerspectiveRepository;
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
import static com.example.arrowdb.auxiliary.FilePathResource.PERSPECTIVE_DIRECTORY;

@Service
@RequiredArgsConstructor
public class DocumentPerspectiveServiceImpl implements DocumentPerspectiveService {

    private final DocumentPerspectiveRepository documentPerspectiveRepository;

    @Override
    @Transactional(rollbackFor = {IOException.class})
    public void saveDocument(DocumentPerspective documentPerspective) {
        documentPerspectiveRepository.save(documentPerspective);
    }

    @Override
    @Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED)
    public DocumentPerspective getDocumentById(Integer id) {
        DocumentPerspective documentPerspective = null;
        Optional<DocumentPerspective> optionalDocument = documentPerspectiveRepository.findById(id);
        if (optionalDocument.isPresent()) {
            documentPerspective = optionalDocument.get();
        }
        return documentPerspective;
    }

    @Override
    @Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED)
    public List<DocumentPerspective> getAllDocuments() {
        return documentPerspectiveRepository.findAll();
    }

    @Override
    @Transactional(rollbackFor = {IOException.class})
    public void deleteDocumentById(Integer id) {
        DocumentPerspective documentPerspective = getDocumentById(id);
        Path path = Paths.get(new File(PERSPECTIVE_DIRECTORY) + "/" + documentPerspective.getKey());
        try {
            Files.delete(path);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        documentPerspectiveRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED)
    public Resource downloadDocumentByKey(String key) throws IOException {
        Path path = Paths.get(new File(PERSPECTIVE_DIRECTORY) + "/" + key);
        Resource resource = new UrlResource(path.toUri());
        if (resource.exists() || resource.isReadable()) {
            return resource;
        } else {
            throw new IOException();
        }
    }

    @Override
    @Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED)
    public List<DocumentPerspective> findAllByIndex(int index, int id) {
        return documentPerspectiveRepository.findAllByIndex(index, id);
    }
}