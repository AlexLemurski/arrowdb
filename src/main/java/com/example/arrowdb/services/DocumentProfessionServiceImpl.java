package com.example.arrowdb.services;

import com.example.arrowdb.entity.DocumentProfession;
import com.example.arrowdb.repositories.DocumentProfessionRepository;
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
public class DocumentProfessionServiceImpl implements DocumentProfessionService {

    private final DocumentProfessionRepository documentProfessionRepository;

    @Override
    @Transactional(rollbackFor = {IOException.class})
    public void saveDocument(DocumentProfession documentDepartment) {
        documentProfessionRepository.save(documentDepartment);
    }

    @Override
    @Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED)
    public DocumentProfession getDocumentById(Integer id) {
        DocumentProfession documentProfession = null;
        Optional<DocumentProfession> optionalDocument = documentProfessionRepository.findById(id);
        if (optionalDocument.isPresent()) {
            documentProfession = optionalDocument.get();
        }
        return documentProfession;
    }

    @Override
    @Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED)
    public List<DocumentProfession> getAllDocuments() {
        return documentProfessionRepository.findAll();
    }

    @Override
    @Transactional(rollbackFor = {IOException.class})
    public void deleteDocumentById(Integer id) {
        DocumentProfession documentProfession = getDocumentById(id);
        Path path = Paths.get(new File(FILES_DIRECTORY) + "/" + documentProfession.getKey());
        try {
            Files.delete(path);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        documentProfessionRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED)
    public Resource downloadDocumentByKey(String key) throws IOException {
        Path path = Paths.get(new File(FILES_DIRECTORY) + "/" + key);
        Resource resource = new UrlResource(path.toUri());
        if(resource.exists() || resource.isReadable()) {
            return resource;
        } else {
            throw new IOException();
        }
    }
}