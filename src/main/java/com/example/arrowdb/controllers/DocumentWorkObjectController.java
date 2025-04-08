package com.example.arrowdb.controllers;

import com.example.arrowdb.entity.DocumentWorkObject;
import com.example.arrowdb.entity.Users;
import com.example.arrowdb.services.DocumentWorkObjectService;
import com.example.arrowdb.services.UsersService;
import com.example.arrowdb.services.WorkObjectService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;

import static com.example.arrowdb.auxiliary.FilePathResource.FILES_DIRECTORY;
import static com.example.arrowdb.auxiliary.KeyGenerator.generateKey;

@Controller
@RequiredArgsConstructor
public class DocumentWorkObjectController {

    private final DocumentWorkObjectService documentWorkObjectService;
    private final WorkObjectService workObjectService;
    private final UsersService usersService;

    @GetMapping("/general/workObject/workObjectDocument/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_WORK_OBJECT_DOCUMENTS', 'ROLE_WORK_OBJECT_VIEW')")
    public String getAllDocuments(@PathVariable("id") Integer id, Model model) {
        model.addAttribute("workObject", workObjectService.findWorkObjectById(id));
        model.addAttribute("documentListProject", documentWorkObjectService.findAllByIndex(0, id));
        model.addAttribute("documentListWork", documentWorkObjectService.findAllByIndex(1, id));
        model.addAttribute("documentListExec", documentWorkObjectService.findAllByIndex(2, id));
        return "work_object/work_object_documents-menu";
    }

    @SneakyThrows
    @PostMapping("/general/workObject/workObjectViewProject/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_WORK_OBJECT_DOCUMENTS')")
    public String uploadMultipleFilesProject(@PathVariable("id") Integer id,
                                           @RequestParam("files") MultipartFile[] files) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        Users users = usersService.findUsersByUserName(userDetails.getUsername());
        for (MultipartFile file : files) {
            String key = generateKey(file.getName());
            DocumentWorkObject documentWorkObject = new DocumentWorkObject(file.getOriginalFilename(),
                    file.getContentType(), key, users, LocalDateTime.now(), workObjectService
                    .findWorkObjectById(id), 0);
            Path path = Paths.get(String.valueOf(new File(FILES_DIRECTORY)), key);
            Path filePath = Files.createFile(path);
            try (FileOutputStream stream = new FileOutputStream(filePath.toString())) {
                stream.write(file.getBytes());
            }
            documentWorkObjectService.saveDocument(documentWorkObject);
        }
        return "redirect:/general/workObject/workObjectDocument/%d".formatted(id);
    }

    @SneakyThrows
    @PostMapping("/general/workObject/workObjectViewWork/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_WORK_OBJECT_DOCUMENTS')")
    public String uploadMultipleFilesWork(@PathVariable("id") Integer id,
                                             @RequestParam("files") MultipartFile[] files) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        Users users = usersService.findUsersByUserName(userDetails.getUsername());
        for (MultipartFile file : files) {
            String key = generateKey(file.getName());
            DocumentWorkObject documentWorkObject = new DocumentWorkObject(file.getOriginalFilename(),
                    file.getContentType(), key, users, LocalDateTime.now(), workObjectService
                    .findWorkObjectById(id), 1);
            Path path = Paths.get(String.valueOf(new File(FILES_DIRECTORY)), key);
            Path filePath = Files.createFile(path);
            try (FileOutputStream stream = new FileOutputStream(filePath.toString())) {
                stream.write(file.getBytes());
            }
            documentWorkObjectService.saveDocument(documentWorkObject);
        }
        return "redirect:/general/workObject/workObjectDocument/%d".formatted(id);
    }

    @SneakyThrows
    @PostMapping("/general/workObject/workObjectViewExec/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_WORK_OBJECT_DOCUMENTS')")
    public String uploadMultipleFilesExec(@PathVariable("id") Integer id,
                                          @RequestParam("files") MultipartFile[] files) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        Users users = usersService.findUsersByUserName(userDetails.getUsername());
        for (MultipartFile file : files) {
            String key = generateKey(file.getName());
            DocumentWorkObject documentWorkObject = new DocumentWorkObject(file.getOriginalFilename(),
                    file.getContentType(), key, users, LocalDateTime.now(), workObjectService
                    .findWorkObjectById(id), 2);
            Path path = Paths.get(String.valueOf(new File(FILES_DIRECTORY)), key);
            Path filePath = Files.createFile(path);
            try (FileOutputStream stream = new FileOutputStream(filePath.toString())) {
                stream.write(file.getBytes());
            }
            documentWorkObjectService.saveDocument(documentWorkObject);
        }
        return "redirect:/general/workObject/workObjectDocument/%d".formatted(id);
    }

    @GetMapping("/general/documentWorkObject/downloadFile/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_WORK_OBJECT_DOCUMENTS', 'ROLE_WORK_OBJECT_VIEW')")
    public ResponseEntity<Resource> downloadFile(@PathVariable("id") Integer id) throws IOException {
        DocumentWorkObject documentWorkObject = documentWorkObjectService.getDocumentById(id);
        Resource resource = documentWorkObjectService.downloadDocumentByKey(documentWorkObject.getKey());
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(documentWorkObject.getDocType()))
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment:filename=\"" +
                                URLEncoder.encode(documentWorkObject.getDocName(), StandardCharsets.UTF_8) + "\"")
                .body(resource);
    }

    @GetMapping("/general/documentWorkObject/documentDeleteProject/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_WORK_OBJECT_DOCUMENTS')")
    public String deleteDocumentEquip(@PathVariable("id") Integer id) {
        int depId = documentWorkObjectService.getDocumentById(id).getWorkObject().getWorkObjectId();
        documentWorkObjectService.deleteDocumentById(id);
        return "redirect:/general/workObject/workObjectDocument/%d".formatted(depId);
    }

    @GetMapping("/general/documentWorkObject/documentDeleteWork/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_WORK_OBJECT_DOCUMENTS')")
    public String deleteDocumentWork(@PathVariable("id") Integer id) {
        int depId = documentWorkObjectService.getDocumentById(id).getWorkObject().getWorkObjectId();
        documentWorkObjectService.deleteDocumentById(id);
        return "redirect:/general/workObject/workObjectDocument/%d".formatted(depId);
    }

    @GetMapping("/general/documentWorkObject/documentDeleteExec/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_WORK_OBJECT_DOCUMENTS')")
    public String deleteDocumentBonus(@PathVariable("id") Integer id) {
        int depId = documentWorkObjectService.getDocumentById(id).getWorkObject().getWorkObjectId();
        documentWorkObjectService.deleteDocumentById(id);
        return "redirect:/general/workObject/workObjectDocument/%d".formatted(depId);
    }

}