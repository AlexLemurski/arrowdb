package com.example.arrowdb.controllers;

import com.example.arrowdb.services.DocumentCommunityDeleteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class DocumentCommunityDeleteController {

    private final DocumentCommunityDeleteService documentCommunityDeleteService;

    @GetMapping("/general/documentCommunity/delete_List")
    public String getAllDocuments(Model model) {
        model.addAttribute("documentDeletedList",
                documentCommunityDeleteService.getAllDeleteDocuments());
        return "documents/documents_deleted-menu";
    }
}