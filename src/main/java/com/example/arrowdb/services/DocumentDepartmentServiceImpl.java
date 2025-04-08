package com.example.arrowdb.services;

import com.example.arrowdb.entity.DocumentDepartment;
import com.example.arrowdb.repositories.DocumentDepartmentRepository;
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
public class DocumentDepartmentServiceImpl implements DocumentDepartmentService {

    private final DocumentDepartmentRepository documentDepartmentRepository;

    @Override
    @Transactional(rollbackFor = {IOException.class})
    public void saveDocument(DocumentDepartment documentDepartment) {
        documentDepartmentRepository.save(documentDepartment);
    }

    @Override
    @Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED)
    public DocumentDepartment getDocumentById(Integer id) {
        DocumentDepartment documentDepartment = null;
        Optional<DocumentDepartment> optionalDocument = documentDepartmentRepository.findById(id);
        if (optionalDocument.isPresent()) {
            documentDepartment = optionalDocument.get();
        }
        return documentDepartment;
    }

    @Override
    @Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED)
    public List<DocumentDepartment> getAllDocuments() {
        return documentDepartmentRepository.findAll();
    }

    @Override
    @Transactional(rollbackFor = {IOException.class})
    public void deleteDocumentById(Integer id) {
        DocumentDepartment documentDepartment = getDocumentById(id);
        Path path = Paths.get(new File(FILES_DIRECTORY) + "/" + documentDepartment.getKey());
        try {
            Files.delete(path);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        documentDepartmentRepository.deleteById(id);
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