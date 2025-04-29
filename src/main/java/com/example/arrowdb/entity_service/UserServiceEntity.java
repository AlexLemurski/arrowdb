package com.example.arrowdb.entity_service;

import com.example.arrowdb.services.RoleService;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.List;

@Service
public class UserServiceEntity {

    public void getRoles(@NotNull Model model, RoleService roleService){
        model.addAttribute("employee", roleService.findRolesByMenuName("employee"));
        model.addAttribute("profession", roleService.findRolesByMenuName("profession"));
        model.addAttribute("department", roleService.findRolesByMenuName("department"));
        model.addAttribute("schedule", roleService.findRolesByMenuName("schedule"));
        model.addAttribute("vocation", roleService.findRolesByMenuName("vocation"));
        model.addAttribute("store_personal", roleService.findRolesByMenuName("store_personal"));
        model.addAttribute("store_work", roleService.findRolesByMenuName("store_work"));
        model.addAttribute("store_meas", roleService.findRolesByMenuName("store_meas"));
        model.addAttribute("store_scloth", roleService.findRolesByMenuName("store_scloth"));
        model.addAttribute("activity_work", roleService.findRolesByMenuName("activity_work"));
        model.addAttribute("activity_control", roleService.findRolesByMenuName("activity_control"));
        model.addAttribute("activity_doc", roleService.findRolesByMenuName("activity_doc"));
        model.addAttribute("perspective_doc", roleService.findRolesByMenuName("perspective_doc"));
    }
}