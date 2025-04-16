package com.example.arrowdb.controllers;

import com.example.arrowdb.auxiliary.ExcelReader;
import com.example.arrowdb.entity.DocumentPerspective;
import com.example.arrowdb.entity.PerspectiveObject;
import com.example.arrowdb.entity.Users;
import com.example.arrowdb.services.DocumentPerspectiveService;
import com.example.arrowdb.services.PerspectiveObjectService;
import com.example.arrowdb.services.UsersService;
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
import static com.example.arrowdb.auxiliary.FilePathResource.PERSPECTIVE_DIRECTORY;
import static com.example.arrowdb.auxiliary.KeyGenerator.generateKey;

@Controller
@RequiredArgsConstructor
public class DocumentPerspectiveController {

    private final DocumentPerspectiveService documentPerspectiveService;
    private final PerspectiveObjectService perspectiveObjectService;
    private final UsersService usersService;
    private final ExcelReader excelReader;

    @GetMapping("/general/perspective/perspectiveDocument/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_PERSPECTIVE_DOCUMENTS', 'ROLE_PERSPECTIVE_VIEW')")
    public String getAllDocuments(@PathVariable("id") Integer id, Model model) {
        model.addAttribute("perspectiveObject", perspectiveObjectService.findPerspectiveObjectById(id));
        model.addAttribute("documentListEquip", documentPerspectiveService.findAllByIndex(0, id));
        model.addAttribute("documentListWork", documentPerspectiveService.findAllByIndex(1, id));
        model.addAttribute("documentListBonus", documentPerspectiveService.findAllByIndex(2, id));
        return "perspective/perspective_documents-menu";
    }

    @SneakyThrows
    @PostMapping("/general/perspective/perspectiveViewEquip/{id}")
//    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    public String uploadMultipleFilesEquip(@PathVariable("id") Integer id,
                                      @RequestParam("files") MultipartFile file) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        Users users = usersService.findUsersByUserName(userDetails.getUsername());
//        for (MultipartFile file : files) {
            String key = generateKey(file.getName());
            DocumentPerspective documentPerspective = new DocumentPerspective(file.getOriginalFilename(),
                    file.getContentType(), key, users, LocalDateTime.now(), perspectiveObjectService
                    .findPerspectiveObjectById(id), 0);
            Path path = Paths.get(String.valueOf(new File(PERSPECTIVE_DIRECTORY)), key);
            Path filePath = Files.createFile(path);
            try (FileOutputStream stream = new FileOutputStream(filePath.toString())) {
                stream.write(file.getBytes());
            }
            PerspectiveObject perspectiveObject = perspectiveObjectService.findPerspectiveObjectById(id);
            String filePathForParsing = PERSPECTIVE_DIRECTORY + "\\" + key;
            documentPerspectiveService.saveDocument(documentPerspective);
            perspectiveObject.setTotalPrice(excelReader
                    .getValueOfEquipments(filePathForParsing, 0, 3, 3 ));
            perspectiveObject.setSuccessCount(excelReader
                    .getValueOfEquipments(filePathForParsing, 0, 3, 4 ));
            perspectiveObject.setTotalCount(excelReader
                    .getValueOfEquipments(filePathForParsing, 0, 3, 5 ));
            perspectiveObject.setPercentOfSuccess(excelReader
                    .getValueOfEquipments(filePathForParsing, 0, 3, 6 ));
            perspectiveObjectService.savePerspectiveObject(perspectiveObject);
//        }
        return "redirect:/general/perspective/perspectiveView/%d".formatted(id);
    }

    @SneakyThrows
    @PostMapping("/general/perspective/perspectiveViewWork/{id}")
//    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_PERSPECTIVE_DOCUMENTS')")
    public String uploadMultipleFilesWork(@PathVariable("id") Integer id,
                                           @RequestParam("files") MultipartFile file) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        Users users = usersService.findUsersByUserName(userDetails.getUsername());
//        for (MultipartFile file : files) {
            String key = generateKey(file.getName());
            DocumentPerspective documentPerspective = new DocumentPerspective(file.getOriginalFilename(),
                    file.getContentType(), key, users, LocalDateTime.now(), perspectiveObjectService
                    .findPerspectiveObjectById(id), 1);
            Path path = Paths.get(String.valueOf(new File(PERSPECTIVE_DIRECTORY)), key);
            Path filePath = Files.createFile(path);
            try (FileOutputStream stream = new FileOutputStream(filePath.toString())) {
                stream.write(file.getBytes());
//            }
            documentPerspectiveService.saveDocument(documentPerspective);
        }
        return "redirect:/general/perspective/perspectiveView/%d".formatted(id);
    }

    @SneakyThrows
    @PostMapping("/general/perspective/perspectiveViewBonus/{id}")
//    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_PERSPECTIVE_DOCUMENTS')")
    public String uploadMultipleFilesBonus(@PathVariable("id") Integer id,
                                          @RequestParam("files") MultipartFile[] files) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        Users users = usersService.findUsersByUserName(userDetails.getUsername());
        for (MultipartFile file : files) {
            String key = generateKey(file.getName());
            DocumentPerspective documentPerspective = new DocumentPerspective(file.getOriginalFilename(),
                    file.getContentType(), key, users, LocalDateTime.now(), perspectiveObjectService
                    .findPerspectiveObjectById(id), 2);
            Path path = Paths.get(String.valueOf(new File(PERSPECTIVE_DIRECTORY)), key);
            Path filePath = Files.createFile(path);
            try (FileOutputStream stream = new FileOutputStream(filePath.toString())) {
                stream.write(file.getBytes());
            }
            documentPerspectiveService.saveDocument(documentPerspective);
        }
        return "redirect:/general/perspective/perspectiveView/%d".formatted(id);
    }

    @GetMapping("/general/documentPerspective/downloadFile/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_PERSPECTIVE_DOCUMENTS', 'ROLE_PERSPECTIVE_VIEW')")
    public ResponseEntity<Resource> downloadFile(@PathVariable("id") Integer id) throws IOException {
        DocumentPerspective documentPerspective = documentPerspectiveService.getDocumentById(id);
        Resource resource = documentPerspectiveService.downloadDocumentByKey(documentPerspective.getKey());
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(documentPerspective.getDocType()))
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment:filename=\"" +
                                URLEncoder.encode(documentPerspective.getDocName(), StandardCharsets.UTF_8) + "\"")
                .body(resource);
    }

    @GetMapping("/general/documentPerspective/documentDeleteEquip/{id}")
//    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_PERSPECTIVE_DOCUMENTS')")
    public String deleteDocumentEquip(@PathVariable("id") Integer id) {
        int depId = documentPerspectiveService.getDocumentById(id).getPerspectiveObject().getPerspectiveId();
        PerspectiveObject perspectiveObject = perspectiveObjectService.findPerspectiveObjectById(depId);
        perspectiveObject.setTotalPrice(null);
        perspectiveObject.setSuccessCount(null);
        perspectiveObject.setTotalCount(null);
        perspectiveObject.setPercentOfSuccess(null);
        perspectiveObjectService.savePerspectiveObject(perspectiveObject);
        documentPerspectiveService.deleteDocumentById(id);
        return "redirect:/general/perspective/perspectiveView/%d".formatted(depId);
    }

    @GetMapping("/general/documentPerspective/documentDeleteWork/{id}")
//    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_PERSPECTIVE_DOCUMENTS')")
    public String deleteDocumentWork(@PathVariable("id") Integer id) {
        int depId = documentPerspectiveService.getDocumentById(id).getPerspectiveObject().getPerspectiveId();
        documentPerspectiveService.deleteDocumentById(id);
        return "redirect:/general/perspective/perspectiveView/%d".formatted(depId);
    }

    @GetMapping("/general/documentPerspective/documentDeleteBonus/{id}")
//    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_PERSPECTIVE_DOCUMENTS')")
    public String deleteDocumentBonus(@PathVariable("id") Integer id) {
        int depId = documentPerspectiveService.getDocumentById(id).getPerspectiveObject().getPerspectiveId();
        documentPerspectiveService.deleteDocumentById(id);
        return "redirect:/general/perspective/perspectiveView/%d".formatted(depId);
    }
}