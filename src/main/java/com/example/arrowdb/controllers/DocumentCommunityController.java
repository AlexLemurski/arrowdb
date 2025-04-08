package com.example.arrowdb.controllers;

import com.example.arrowdb.entity.DocumentCommunity;
import com.example.arrowdb.entity.DocumentCommunityDeleted;
import com.example.arrowdb.entity.Users;
import com.example.arrowdb.services.DocumentCommunityDeleteService;
import com.example.arrowdb.services.DocumentCommunityService;
import com.example.arrowdb.services.UsersService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

@Controller
@RequiredArgsConstructor
public class DocumentCommunityController {

    private final DocumentCommunityService documentCommunityService;
    private final DocumentCommunityDeleteService documentCommunityDeleteService;
    private final UsersService usersService;

    @GetMapping("/general/documentCommunity")
    public String getAllDocuments(Model model) {
        model.addAttribute("documentList", documentCommunityService.getAllDocuments());
        return "documents/documents-menu";
    }

    @SneakyThrows
    @PostMapping("/general/documentCommunity/uploadFiles")
    public String uploadMultipleFiles(@RequestParam("files") MultipartFile[] files) {
        for (MultipartFile file : files) {
            documentCommunityService.saveDocument(file);
        }
        return "redirect:/general/documentCommunity";
    }

    @GetMapping("/general/documentCommunity/downloadFile/{id}")
    public ResponseEntity<Resource> downloadFile(@PathVariable("id") Integer id) throws IOException {
        DocumentCommunity documentCommunity = documentCommunityService.getDocumentById(id);
        Resource resource = documentCommunityService.downloadDocumentByKey(documentCommunity.getKey());
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(documentCommunity.getDocType()))
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment:filename=\"" +
                                URLEncoder.encode(documentCommunity.getDocName(), StandardCharsets.UTF_8) + "\"")
                .body(resource);
    }

    @GetMapping("/general/documentCommunity/documentDelete/{id}")
    public String deleteDocument(@PathVariable("id") Integer id,
                                 @ModelAttribute DocumentCommunityDeleted documentCommunityDeleted) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Users users = usersService.findUsersByUserName(userDetails.getUsername());
        DocumentCommunity documentCommunity = documentCommunityService.getDocumentById(id);
        documentCommunityDeleted.setDocName(documentCommunity.getDocName());
        documentCommunityDeleted.setSize(documentCommunity.getSize());
        documentCommunityDeleted.setAuthor(users);
        documentCommunityDeleted.setDateAndTimeLastChange(LocalDateTime.now());
        documentCommunityDeleteService.saveDocument(documentCommunityDeleted);
        documentCommunityService.deleteDocumentById(id);
        return "redirect:/general/documentCommunity";
    }
}