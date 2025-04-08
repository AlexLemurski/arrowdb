package com.example.arrowdb.controllers;

import com.example.arrowdb.entity.Employee;
import com.example.arrowdb.entity.Profession;
import com.example.arrowdb.enums.EmployeeStatusENUM;
import com.example.arrowdb.enums.ProfAndDepStatusENUM;
import com.example.arrowdb.enums.QualityENUM;
import com.example.arrowdb.services.ProfessionService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

import static com.example.arrowdb.auxiliary.Message.*;

@Controller
@RequiredArgsConstructor
public class ProfessionController {

    private final ProfessionService professionService;

    @GetMapping("/general/profession")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_PROFESSION_VIEW')")
    public String getProfessionsList(@NotNull Model model) {
        List<Profession> professionList = professionService.findAllProfessionsForMainMenu();
        model.addAttribute("professionList", professionList);
        model.addAttribute("professionStatusList", ProfAndDepStatusENUM.values());
        return "profession/profession-menu";
    }

    @GetMapping("/general/profession/professionView/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_PROFESSION_VIEW')")
    public String findProfessionById(@PathVariable("id") int id,
                                     Model model) {
        Profession profession = professionService.findProfessionByIdForView(id);
        model.addAttribute("profession", profession);
        return "profession/profession-view";
    }

    @GetMapping("/general/profession/professionCreate")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_PROFESSION_CREATE')")
    public String createProfessionForm(@ModelAttribute Profession profession,
                                       Model model) {
        model.addAttribute("qualityList", QualityENUM.values());
        return "profession/profession-create";
    }

    @PostMapping("/general/profession/professionCreate")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_PROFESSION_CREATE')")
    public String createProfession(@Valid @ModelAttribute Profession profession,
                                   BindingResult bindingResult,
                                   Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("qualityList", QualityENUM.values());
            return "profession/profession-create";
        } else {
            try {
                profession.setProfAndDepStatusENUM(ProfAndDepStatusENUM.ACTIVE);
                profession.setNumOfVacancy(0);
                professionService.saveProfession(profession);
                return "redirect:/general/profession";
            } catch (Exception e) {
                model.addAttribute("error", UNIQUE_PROFESSION);
                model.addAttribute("qualityList", QualityENUM.values());
                return "profession/profession-create";
            }
        }
    }

    @GetMapping("/general/profession/professionDelete/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_PROFESSION_DELETE')")
    public String deleteProfession(@PathVariable("id") int id,
                                   Model model) {
        try {
            professionService.deleteProfessionById(id);
        } catch (Exception e) {
            Profession profession = professionService.findProfessionById(id);
            model.addAttribute("profession", profession);
            model.addAttribute("error", DELETE_PROFESSION_MESSAGE);
            return "error/profession-error-delete";
        }
        return "redirect:/general/profession";
    }

    @GetMapping("/general/profession/professionUpdate/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_PROFESSION_UPDATE')")
    public String updateProfessionForm(@PathVariable("id") int id,
                                       Model model) {
        Profession profession = professionService.findProfessionById(id);
        if (profession.getProfAndDepStatusENUM().equals(ProfAndDepStatusENUM.CLOSED)) {
            return "redirect:/general/profession/professionView/%d".formatted(profession.getProfId());
        }
        model.addAttribute("professionStatus", ProfAndDepStatusENUM.values());
        model.addAttribute("profession", professionService.findProfessionById(id));
        model.addAttribute("qualityList", QualityENUM.values());
        return "profession/profession-update";
    }

    @PostMapping("/general/profession/professionUpdate")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_PROFESSION_UPDATE')")
    public String updateProfession(@Valid @ModelAttribute Profession profession,
                                   BindingResult bindingResult,
                                   Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("qualityList", QualityENUM.values());
            return "profession/profession-update";
        } else {
            Profession professionById = professionService.findProfessionById(profession.getProfId());
            List<Employee> employeeActiveList = professionById.getEmployeeList().stream()
                    .filter(e -> !e.getEmployeeStatusENUM().equals(EmployeeStatusENUM.DRAFT))
                    .filter(e -> !e.getEmployeeStatusENUM().equals(EmployeeStatusENUM.CLOSED))
                    .toList();
            if (!employeeActiveList.isEmpty() && profession.getProfAndDepStatusENUM()
                    .equals(ProfAndDepStatusENUM.CLOSED)) {
                model.addAttribute("employeeActiveList", employeeActiveList);
                model.addAttribute("profession", professionById);
                model.addAttribute("error", CHANGE_STATUS_PROFESSION_MESSAGE);
                return "error/profession-error-update";
            }
            try {
                if (profession.getProfAndDepStatusENUM().equals(ProfAndDepStatusENUM.CLOSED)) {
                    profession.setNumOfVacancy(0);
                }
                model.addAttribute("qualityList", QualityENUM.values());
                professionService.saveProfession(profession);
                return "redirect:/general/profession/professionUpdate/%d".formatted(profession.getProfId());
            } catch (Exception e) {
                model.addAttribute("error", UNIQUE_PROFESSION);
                return "profession/profession-update";
            }
        }
    }
}