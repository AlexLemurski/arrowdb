package com.example.arrowdb.controllers;

import com.example.arrowdb.entity.DocumentDepartment;
import com.example.arrowdb.entity.Users;
import com.example.arrowdb.services.DepartmentService;
import com.example.arrowdb.services.DocumentDepartmentService;
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
public class DocumentDepartmentController {

    private final DocumentDepartmentService documentDepartmentService;
    private final DepartmentService departmentService;
    private final UsersService usersService;

    @GetMapping("/general/department/documentDepartment/uploadFiles")
    public String getAllDocuments(Model model) {
        model.addAttribute("documentList", documentDepartmentService.getAllDocuments());
        return "department/department-view";
    }

    @SneakyThrows
    @PostMapping("/general/department/departmentView/{id}")
    public String uploadMultipleFiles(@PathVariable("id") Integer id,
                                      @RequestParam("files") MultipartFile[] files) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        Users users = usersService.findUsersByUserName(userDetails.getUsername());
        for (MultipartFile file : files) {
            String key = generateKey(file.getName());
            DocumentDepartment documentDepartment = new DocumentDepartment(file.getOriginalFilename(),
                    file.getContentType(), key, users, LocalDateTime.now(), departmentService
                    .findDepartmentById(id));
            Path path = Paths.get(String.valueOf(new File(FILES_DIRECTORY)), key);
            Path filePath = Files.createFile(path);
            try (FileOutputStream stream = new FileOutputStream(filePath.toString())) {
                stream.write(file.getBytes());
            }
            documentDepartmentService.saveDocument(documentDepartment);
        }
        return "redirect:/general/department/departmentView/%d".formatted(id);
    }

    @GetMapping("/general/documentDepartment/downloadFile/{id}")
    public ResponseEntity<Resource> downloadFile(@PathVariable("id") Integer id) throws IOException {
        DocumentDepartment documentDepartment = documentDepartmentService.getDocumentById(id);
        Resource resource = documentDepartmentService.downloadDocumentByKey(documentDepartment.getKey());
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(documentDepartment.getDocType()))
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment:filename=\"" +
                                URLEncoder.encode(documentDepartment.getDocName(), StandardCharsets.UTF_8) + "\"")
                .body(resource);
    }

    @GetMapping("/general/documentDepartment/documentDelete/{id}")
    public String deleteDocument(@PathVariable("id") Integer id) {
        int depId = documentDepartmentService.getDocumentById(id).getDepartment().getDepId();
        documentDepartmentService.deleteDocumentById(id);
        return "redirect:/general/department/departmentView/%d".formatted(depId);
    }
}