package com.example.arrowdb.controllers;

import com.example.arrowdb.entity.DocumentWorkObjectCom;
import com.example.arrowdb.entity.Users;
import com.example.arrowdb.services.DocumentWorkObjectServiceCom;
import com.example.arrowdb.services.UsersService;
import com.example.arrowdb.services.WorkObjectService;
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
public class DocumentWorkObjectControllerCom {

    private final DocumentWorkObjectServiceCom documentWorkObjectServiceCom;
    private final WorkObjectService workObjectService;
    private final UsersService usersService;

    @GetMapping("/general/workobject/documentWorkobject/uploadFiles")
    public String getAllDocuments(Model model) {
        model.addAttribute("documentList", documentWorkObjectServiceCom.getAllDocuments());
        return "work_object/work_object-view";
    }

    @SneakyThrows
    @PostMapping("/general/workobject/workobjectView/{id}")
    public String uploadMultipleFiles(@PathVariable("id") Integer id,
                                      @RequestParam("files") MultipartFile[] files) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        Users users = usersService.findUsersByUserName(userDetails.getUsername());
        for (MultipartFile file : files) {
            String key = generateKey(file.getName());
            DocumentWorkObjectCom documentWorkObjectCom = new DocumentWorkObjectCom(file.getOriginalFilename(),
                    file.getContentType(), key, users, LocalDateTime.now(), workObjectService
                    .findWorkObjectById(id));
            Path path = Paths.get(String.valueOf(new File(FILES_DIRECTORY)), key);
            Path filePath = Files.createFile(path);
            try (FileOutputStream stream = new FileOutputStream(filePath.toString())) {
                stream.write(file.getBytes());
            }
            documentWorkObjectServiceCom.saveDocument(documentWorkObjectCom);
        }
        return "redirect:/general/workobject/workobjectView/%d".formatted(id);
    }

    @GetMapping("/general/documentWorkobject/downloadFile/{id}")
    public ResponseEntity<Resource> downloadFile(@PathVariable("id") Integer id) throws IOException {
        DocumentWorkObjectCom documentWorkObjectCom = documentWorkObjectServiceCom.getDocumentById(id);
        Resource resource = documentWorkObjectServiceCom.downloadDocumentByKey(documentWorkObjectCom.getKey());
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(documentWorkObjectCom.getDocType()))
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment:filename=\"" +
                        URLEncoder.encode(documentWorkObjectCom.getDocName(), StandardCharsets.UTF_8) + "\"")
                .body(resource);
    }

    @GetMapping("/general/documentWorkobject/documentDelete/{id}")
    public String deleteDocument(@PathVariable("id") Integer id) {
        int depId = documentWorkObjectServiceCom.getDocumentById(id).getWorkObject().getWorkObjectId();
        documentWorkObjectServiceCom.deleteDocumentById(id);
        return "redirect:/general/workobject/workobjectView/%d".formatted(depId);
    }

}