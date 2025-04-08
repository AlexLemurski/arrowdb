package com.example.arrowdb.services;

import com.example.arrowdb.entity.DocumentCommunity;
import com.example.arrowdb.entity.Users;
import com.example.arrowdb.repositories.DocumentCommunityRepository;
import com.example.arrowdb.repositories.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.example.arrowdb.auxiliary.FilePathResource.FILES_DIRECTORY;
import static com.example.arrowdb.auxiliary.KeyGenerator.generateKey;

@Service
@RequiredArgsConstructor
public class DocumentCommunityServiceImpl implements DocumentCommunityService {

    private final DocumentCommunityRepository documentCommunityRepository;
    private final UsersService usersService;

    @Override
    @Transactional(rollbackFor = {IOException.class})
    public void saveDocument(MultipartFile file) {
        String key = generateKey(file.getName());
        String size = String.format("%.3f Мб", (double) file.getSize() / 1_000_000);
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Users users = usersService.findUsersByUserName(userDetails.getUsername());
        try {
            DocumentCommunity documentCommunity = new DocumentCommunity(file.getOriginalFilename(),
                    file.getContentType(), size, key, users, LocalDateTime.now());
            documentCommunityRepository.save(documentCommunity);
            Path path = Paths.get(String.valueOf(new File(FILES_DIRECTORY)), key);
            Path filePath = Files.createFile(path);
            try (FileOutputStream stream = new FileOutputStream(filePath.toString())) {
                stream.write(file.getBytes());
            }
        } catch (Exception e) {
            e.getStackTrace();
        }
    }

    @Override
    @Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED)
    public DocumentCommunity getDocumentById(Integer id) {
        DocumentCommunity documentCommunity = null;
        Optional<DocumentCommunity> optionalDocument = documentCommunityRepository.findById(id);
        if (optionalDocument.isPresent()) {
            documentCommunity = optionalDocument.get();
        }
        return documentCommunity;
    }

    @Override
    @Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED)
    public List<DocumentCommunity> getAllDocuments() {
        return documentCommunityRepository.findAll();
    }

    @Override
    @Transactional(rollbackFor = {IOException.class})
    public void deleteDocumentById(Integer id) {
        DocumentCommunity documentCommunity = getDocumentById(id);
        Path path = Paths.get(new File(FILES_DIRECTORY) + "/" + documentCommunity.getKey());
        try {
            Files.delete(path);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        documentCommunityRepository.deleteById(id);
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