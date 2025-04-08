package com.example.arrowdb.controllers;

import com.example.arrowdb.entity.Users;
import com.example.arrowdb.services.UsersService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDate;

@Controller
@RequiredArgsConstructor
public class GeneralController {

    private final UsersService usersService;

    @GetMapping("/general")
    public String genMenu(@AuthenticationPrincipal UserDetails userDetails,
                          Model model) {
        Users users = usersService.findUsersByUserName(userDetails.getUsername());
        model.addAttribute("employee", users.getEmployee());
        model.addAttribute("currentDate", LocalDate.now());
        return "general/general-menu";
    }
}