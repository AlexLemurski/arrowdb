package com.example.arrowdb.services;

import com.example.arrowdb.entity.DocumentWorkObjectCom;
import com.example.arrowdb.repositories.DocumentWorkObjectRepositoryCom;
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
public class DocumentWorkObjectServiceComImpl implements DocumentWorkObjectServiceCom{

    private final DocumentWorkObjectRepositoryCom documentWorkObjectRepositoryCom;

    @Override
    @Transactional(rollbackFor = {IOException.class})
    public void saveDocument(DocumentWorkObjectCom documentWorkObjectCom) {
        documentWorkObjectRepositoryCom.save(documentWorkObjectCom);
    }

    @Override
    @Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED)
    public DocumentWorkObjectCom getDocumentById(Integer id) {
        DocumentWorkObjectCom documentWorkObjectCom = null;
        Optional<DocumentWorkObjectCom> optionalDocument = documentWorkObjectRepositoryCom.findById(id);
        if (optionalDocument.isPresent()) {
            documentWorkObjectCom = optionalDocument.get();
        }
        return documentWorkObjectCom;
    }

    @Override
    @Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED)
    public List<DocumentWorkObjectCom> getAllDocuments() {
        return documentWorkObjectRepositoryCom.findAll();
    }

    @Override
    @Transactional(rollbackFor = {IOException.class})
    public void deleteDocumentById(Integer id) {
        DocumentWorkObjectCom documentWorkObjectCom = getDocumentById(id);
        Path path = Paths.get(new File(FILES_DIRECTORY) + "/" + documentWorkObjectCom.getKey());
        try {
            Files.delete(path);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        documentWorkObjectRepositoryCom.deleteById(id);
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
}
