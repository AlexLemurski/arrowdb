package com.example.arrowdb.controllers;

import com.example.arrowdb.entity.ConstructionControl;
import com.example.arrowdb.entity.Employee;
import com.example.arrowdb.entity.WorkObject;
import com.example.arrowdb.enums.ConstructionControlStatusENUM;
import com.example.arrowdb.enums.WorkObjectStatusENUM;
import com.example.arrowdb.services.ConstructionControlService;
import com.example.arrowdb.services.EmployeeService;
import com.example.arrowdb.services.WorkObjectService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import static com.example.arrowdb.auxiliary.Message.UNIQUE_CONST_CONTROL;

@Controller
@RequiredArgsConstructor
public class ConstructionControlController {

    private final EmployeeService employeeService;
    private final WorkObjectService workObjectService;
    private final ConstructionControlService constructionControlService;

    @GetMapping("/general/constr_control")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_CONSTR_CONTROL_VIEW')")
    public String findAllConstructionControl(@AuthenticationPrincipal UserDetails userDetails,
                                             Model model) {
        List<WorkObject> workObjectList = workObjectService.findAllConstructionControlFoMainMenu().stream()
                .filter(e -> !e.getWorkObjectStatusENUM().equals(WorkObjectStatusENUM.NOT_STARTED))
                .toList();
        model.addAttribute("workObjectList", workObjectList);
        model.addAttribute("adminAccept",
                userDetails.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN")));
        model.addAttribute("roleDraft",
                userDetails.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_CONSTR_CONTROL_DRAFT")));
        model.addAttribute("roleUpdate",
                userDetails.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_CONSTR_CONTROL_UPDATE")));
        return "constr_control/constr_control-menu";
    }

    @GetMapping("/general/constr_control/constr_controlWarnings/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_CONSTR_CONTROL_VIEW')")
    public String findAllWarningsControl(@PathVariable("id") int id,
                                         @AuthenticationPrincipal UserDetails userDetails,
                                         Model model) {
        WorkObject workObject = workObjectService.findWorkObjectByIdForConstructionControl(id);
        model.addAttribute("workObject", workObject);
        model.addAttribute("userName", userDetails.getUsername());
        model.addAttribute("adminAccept",
                userDetails.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN")));
        model.addAttribute("roleDraft",
                userDetails.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_CONSTR_CONTROL_DRAFT")));
        model.addAttribute("roleUpdate",
                userDetails.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_CONSTR_CONTROL_UPDATE")));
        if(userDetails.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_CONSTR_CONTROL_DRAFT")) ||
                userDetails.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_CONSTR_CONTROL_UPDATE")) ||
           userDetails.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))){
            model.addAttribute("constructionControlList", workObject.getConstructionControlList()
                    .stream()
                    .sorted(Comparator.comparingInt(ConstructionControl::getConstrControlId)).toList());
        } else {
            model.addAttribute("constructionControlList", workObject.getConstructionControlList()
                    .stream()
                    .filter(e -> !e.getConstructionControlStatusENUM().equals(ConstructionControlStatusENUM.DRAFT))
                    .toList());
        }
        return "constr_control/constr_control-warnings";
    }

    @GetMapping("/general/constr_control/constr_controlCreate")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_CONSTR_CONTROL_CREATE')")
    public String createConstructionControlForm(@ModelAttribute ConstructionControl constructionControl,
                                                Model model) {
        model.addAttribute("employeeList", employeeService.findAllActiveEmployees());
        model.addAttribute("workObjectsList", workObjectService.findAllWorkObjects().stream()
                .filter(e -> e.getWorkObjectStatusENUM().equals(WorkObjectStatusENUM.ACTIVE))
                .toList());
        model.addAttribute("warningStatusList", Arrays.stream(ConstructionControlStatusENUM.values())
                .filter(e -> !e.equals(ConstructionControlStatusENUM.CLOSED))
                .toList());
        return "constr_control/constr_control-create";
    }

    @PostMapping("/general/constr_control/constr_controlCreate")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_CONSTR_CONTROL_CREATE')")
    public String creatConstructionControl(@Valid ConstructionControl constructionControl,
                                           BindingResult bindingResult,
                                           Model model) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<Employee> employeeList = employeeService.findAllActiveEmployees();
        List<WorkObject> workObjectsList = workObjectService.findAllWorkObjects().stream()
                .filter(e -> e.equals(ConstructionControlStatusENUM.ACTIVE))
                .toList();
        if (bindingResult.hasErrors()) {
            model.addAttribute("employeeList", employeeList);
            model.addAttribute("workObjectsList", workObjectsList);
            model.addAttribute("warningStatusList", ConstructionControlStatusENUM.values());
            return "constr_control/constr_control-create";
        } else {
            try {
                constructionControl.setAuthor(userDetails.getUsername());
                constructionControlService.saveConstructionControl(constructionControl);
                return "redirect:/general/constr_control/constr_controlWarnings/%d"
                        .formatted(constructionControl.getWorkObject().getWorkObjectId());
            } catch (Exception e) {
                model.addAttribute("employeeList", employeeList);
                model.addAttribute("workObjectsList", workObjectsList);
                model.addAttribute("warningStatusList", ConstructionControlStatusENUM.values());
                model.addAttribute("errorUniq", UNIQUE_CONST_CONTROL);
                return "constr_control/constr_control-create";
            }
        }
    }

    @GetMapping("/general/constr_control/constr_controlDelete/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_CONSTR_CONTROL_DRAFT')")
    public String deleteConstructionControl(@PathVariable("id") int id){
        ConstructionControl constructionControl = constructionControlService.findConstructionControlById(id);
        constructionControlService.deleteConstructionControlById(id);
        return "redirect:/general/constr_control/constr_controlWarnings/%d"
                .formatted(constructionControl.getWorkObject().getWorkObjectId());
    }

    @GetMapping("/general/constr_control/constr_controlUpdate/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_CONSTR_CONTROL_UPDATE')")
    public String updateConstructionControlForm(@PathVariable("id") int id,
                                                Model model) {
        ConstructionControl constructionControl = constructionControlService.findConstructionControlById(id);
        if (constructionControl.getConstructionControlStatusENUM().equals(ConstructionControlStatusENUM.CLOSED)) {
            return "redirect:/general/constr_control/constr_controlWarnings/%d"
                    .formatted(constructionControl.getWorkObject().getWorkObjectId());
        }
        model.addAttribute("constructionControl", constructionControl);
        model.addAttribute("employeeList", employeeService.findAllActiveEmployees());
        model.addAttribute("workObjectsList", new ArrayList<>(workObjectService.findAllWorkObjects()
                .stream()
                .filter(e -> e.getWorkObjectStatusENUM().equals(WorkObjectStatusENUM.ACTIVE))
                .toList()));
        if(constructionControl.getConstructionControlStatusENUM().equals(ConstructionControlStatusENUM.ACTIVE)){
            model.addAttribute("warningStatusList", Arrays.stream(ConstructionControlStatusENUM
                            .values()).filter(e -> !e.equals(ConstructionControlStatusENUM.DRAFT)));
        }
        else if(constructionControl.getConstructionControlStatusENUM().equals(ConstructionControlStatusENUM.DRAFT)){
            model.addAttribute("warningStatusList", Arrays.stream(ConstructionControlStatusENUM
                            .values()).filter(e -> !e.equals(ConstructionControlStatusENUM.CLOSED)));
        } else {
            model.addAttribute("warningStatusList", ConstructionControlStatusENUM.values());
        }
        return "constr_control/constr_control-update";
    }

    @PostMapping("/general/constr_control/constr_controlUpdate")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_CONSTR_CONTROL_UPDATE')")
    public String updateConstructionControlForm(@Valid @ModelAttribute ConstructionControl constructionControl,
                                                BindingResult bindingResult,
                                                Model model) {
        List<Employee> employeeList = new ArrayList<>(employeeService.findAllActiveEmployees());
        List<WorkObject> workObjectsList = new ArrayList<>(workObjectService.findAllWorkObjects().stream()
                .filter(e -> e.getWorkObjectStatusENUM().equals(WorkObjectStatusENUM.ACTIVE))
                .toList());
        if (bindingResult.hasErrors()) {
            model.addAttribute("employeeList", employeeList);
            model.addAttribute("workObjectsList", workObjectsList);
            if(constructionControl.getConstructionControlStatusENUM().equals(ConstructionControlStatusENUM.ACTIVE)){
                model.addAttribute("warningStatusList", Arrays.stream(ConstructionControlStatusENUM
                                .values()).filter(e -> !e.equals(ConstructionControlStatusENUM.DRAFT)));
            }
            else if(constructionControl.getConstructionControlStatusENUM().equals(ConstructionControlStatusENUM.DRAFT)){
                model.addAttribute("warningStatusList", Arrays.stream(ConstructionControlStatusENUM
                                .values()).filter(e -> !e.equals(ConstructionControlStatusENUM.CLOSED)));
            } else {
                model.addAttribute("warningStatusList", ConstructionControlStatusENUM.values());
            }
            return "constr_control/constr_control-update";
        } else {
            if (constructionControl.getConstructionControlStatusENUM().equals(ConstructionControlStatusENUM.CLOSED)) {
                constructionControl.setResponsibleFromCustomer(null);
                constructionControl.setResponsibleFromContractor(null);
                constructionControl.setResponsibleFromSKContractor(null);
                constructionControl.setResponsibleFromSubContractor(null);
            }
            try {
                constructionControlService.saveConstructionControl(constructionControl);
                return "redirect:/general/constr_control/constr_controlWarnings/%d"
                        .formatted(constructionControl.getWorkObject().getWorkObjectId());
            } catch (Exception e) {
                model.addAttribute("employeeList", employeeList);
                model.addAttribute("workObjectsList", workObjectsList);
                if(constructionControl.getConstructionControlStatusENUM().equals(ConstructionControlStatusENUM.ACTIVE)){
                    model.addAttribute("warningStatusList", Arrays.stream(ConstructionControlStatusENUM
                                    .values()).filter(p -> !p.equals(ConstructionControlStatusENUM.DRAFT)));
                }
                else if(constructionControl.getConstructionControlStatusENUM().equals(ConstructionControlStatusENUM.DRAFT)){
                    model.addAttribute("warningStatusList", Arrays.stream(ConstructionControlStatusENUM
                                    .values()).filter(p -> p.equals(ConstructionControlStatusENUM.DRAFT)));
                } else {
                    model.addAttribute("warningStatusList", ConstructionControlStatusENUM.values());
                }
                model.addAttribute("errorUniq", UNIQUE_CONST_CONTROL);
                return "constr_control/constr_control-update";
            }
        }
    }
}