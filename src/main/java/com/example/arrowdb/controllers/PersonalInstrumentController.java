package com.example.arrowdb.controllers;

import com.example.arrowdb.entity.Employee;
import com.example.arrowdb.entity.PersonalInstrument;
import com.example.arrowdb.entity.PersonalInstrumentCondition;
import com.example.arrowdb.enums.EmployeeStatusENUM;
import com.example.arrowdb.enums.PersonalConditionENUM;
import com.example.arrowdb.enums.TechnicalConditionENUM;
import com.example.arrowdb.repositories.PersonalInstrumentConditionAUDRepository;
import com.example.arrowdb.services.EmployeeService;
import com.example.arrowdb.services.PersonalInstrumentConditionService;
import com.example.arrowdb.services.PersonalInstrumentService;
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

import java.util.Arrays;
import java.util.List;

import static com.example.arrowdb.auxiliary.Message.DELETE_INSTRUMENT_MESSAGE;
import static com.example.arrowdb.auxiliary.Message.UNIQUE_INSTR_INV;

@Controller
@RequiredArgsConstructor
public class PersonalInstrumentController {

    private final EmployeeService employeeService;
    private final PersonalInstrumentService personalInstrumentService;
    private final PersonalInstrumentConditionService personalInstrumentConditionService;
    private final PersonalInstrumentConditionAUDRepository personalInstrumentConditionAUDRepository;

    @GetMapping("/general/p_instrument/catalog")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_STORE_PERS_INSTR_VIEW')")
    public String getPersonalInstrumentList(Model model) {
        model.addAttribute("personalInstrument",
                personalInstrumentService.findPersonalInstrumentForCatalogueMenu());
        model.addAttribute("TechnicalConditionENUM", TechnicalConditionENUM.values());
        model.addAttribute("PersonalConditionENUM", PersonalConditionENUM.values());
        return "stock/p_instrument_catalog";
    }

    @GetMapping("/general/p_instrument")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_STORE_PERS_INSTR_VIEW')")
    public String getPersonalInstrumentListEmployee(Model model) {
        model.addAttribute("employee", employeeService.findAllEmployeesForPersonalInstrument());
        model.addAttribute("employeeStatusList", Arrays.stream(EmployeeStatusENUM.values())
                .filter(e -> !e.getTitle().equals("Закрыт")));
        return "stock/p_instrument-com_table";
    }

    @GetMapping("/general/p_instrument/p_instrumentView/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_STORE_PERS_INSTR_VIEW')")
    public String findPersonalInstrumentById(@PathVariable("id") int id,
                                             Model model) {
        model.addAttribute("personalInstrument", personalInstrumentService
                .findPersonalInstrumentById(id));
        model.addAttribute("personalInstrumentConditionAUD", personalInstrumentConditionAUDRepository
                .findAllPersonalInstrumentConditionAUDById(id));
        return "stock/p_instrument-view";
    }

    @GetMapping("/general/p_instrument/p_instrumentCreate")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_STORE_PERS_INSTR_CREATE')")
    public String createPersonalInstrumentForm(@ModelAttribute(value = "personalInstrument")
                                                   PersonalInstrument personalInstrument) {
        return "stock/p_instrument-create";
    }

    @PostMapping("/general/p_instrument/p_instrumentCreate")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_STORE_PERS_INSTR_CREATE')")
    public String createPersonalInstrument(@Valid @ModelAttribute PersonalInstrument personalInstrument,
                                           BindingResult bindingResult,
                                           Model model) {
        if (bindingResult.hasErrors()) {
            return "stock/p_instrument-create";
        } else {
            try {
                personalInstrument.setTechnicalConditionENUM(TechnicalConditionENUM.OK);
                personalInstrument.setPersonalConditionENUM(PersonalConditionENUM.NOT_ISSUED);
                personalInstrumentService.savePersonalInstrument(personalInstrument);
                PersonalInstrumentCondition personalInstrumentCondition = new PersonalInstrumentCondition();
                personalInstrumentCondition.setTechnicalConditionENUM(TechnicalConditionENUM.OK);
                personalInstrumentCondition.setPersonalConditionENUM(PersonalConditionENUM.NOT_ISSUED);
                personalInstrumentCondition.setPersonalInstrId(personalInstrument.getPersonalInstrId());
                personalInstrumentConditionService.savePersonalInstrument(personalInstrumentCondition);
                return "redirect:/general/p_instrument/catalog";
            } catch (Exception e) {
                model.addAttribute("errorInv", UNIQUE_INSTR_INV);
                return "stock/p_instrument-create";
            }
        }
    }

    @GetMapping("/general/p_instrument/p_instrumentDelete/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_STORE_PERS_INSTR_DELETE')")
    public String deletePersonalInstrument(@PathVariable("id") int id,
                                           Model model) {
        PersonalInstrument personalInstrument = personalInstrumentService.findPersonalInstrumentById(id);
        model.addAttribute("personalInstrument", personalInstrument);
        model.addAttribute("error", DELETE_INSTRUMENT_MESSAGE);
        if (personalInstrument.getEmployee() != null) {
            return "error/p_instrument-error";
        } else {
            personalInstrumentService.deletePersonalInstrumentsById(id);
            personalInstrumentConditionService.deletePersonalInstrumentsById(id);
            return "redirect:/general/p_instrument/catalog";
        }
    }

    @GetMapping("/general/p_instrument/p_instrumentUpdate/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_STORE_PERS_INSTR_UPDATE')")
    public String updateInstrumentForm(@PathVariable("id") int id, Model model) {
        PersonalInstrument personalInstrument = personalInstrumentService.findPersonalInstrumentById(id);
        if (personalInstrument.getTechnicalConditionENUM().equals(TechnicalConditionENUM.OUT)) {
            return "redirect:/general/p_instrument/p_instrumentView/%d"
                    .formatted(personalInstrument.getPersonalInstrId());
        }
        List<Employee> employeeList = employeeService.findAllEmployeeForMainMenu().stream()
                .filter(e -> e.getEmployeeStatusENUM().equals(EmployeeStatusENUM.ACTIVE))
                .toList();
        if (!personalInstrument.getTechnicalConditionENUM().equals(TechnicalConditionENUM.OK)) {
            personalInstrument.setPersonalConditionENUM(PersonalConditionENUM.NOT_ISSUED);
        }
        model.addAttribute("employeeList", employeeList);
        model.addAttribute("personalInstrument", personalInstrument);
        if (employeeList.isEmpty()) {
            model.addAttribute("conditionForPersonal", Arrays.stream(PersonalConditionENUM.values())
                    .filter(e -> e.equals(PersonalConditionENUM.NOT_ISSUED)).toList());
        } else {
            model.addAttribute("conditionForPersonal", PersonalConditionENUM.values());
        }
        model.addAttribute("conditionForTechn", TechnicalConditionENUM.values());
        return "stock/p_instrument-update";
    }

    @PostMapping("/general/p_instrument/p_instrumentUpdate")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_STORE_PERS_INSTR_UPDATE')")
    public String updateInstrument(@Valid @ModelAttribute PersonalInstrument personalInstrument,
                                   @NotNull BindingResult bindingResult,
                                   Model model) {
        List<Employee> employeeList = employeeService.findAllActiveEmployees();
        PersonalInstrumentCondition personalInstrumentCondition =
                personalInstrumentConditionService.findPersonalInstrumentById(personalInstrument.getPersonalInstrId());
        if (bindingResult.hasErrors()) {
            model.addAttribute("employeeList", employeeList);
            model.addAttribute("conditionForTechn", TechnicalConditionENUM.values());
            if (employeeList.isEmpty()) {
                model.addAttribute("conditionForPersonal", Arrays.stream(PersonalConditionENUM.values())
                        .filter(e -> e.equals(PersonalConditionENUM.NOT_ISSUED)).toList());
            } else {
                model.addAttribute("conditionForPersonal", PersonalConditionENUM.values());
            }
            return "stock/p_instrument-update";
        } else {
            if (!personalInstrument.getTechnicalConditionENUM().equals(TechnicalConditionENUM.OUT)) {
                personalInstrument.setCloseDate(null);
            }
            if (!personalInstrument.getPersonalConditionENUM().equals(PersonalConditionENUM.ISSUED)) {
                personalInstrument.setIssueDate(null);
                personalInstrument.setPersonalConditionENUM(PersonalConditionENUM.NOT_ISSUED);
                personalInstrumentCondition.setIssueDate(null);
                personalInstrumentCondition.setPersonalConditionENUM(PersonalConditionENUM.NOT_ISSUED);
            }
            if (!personalInstrument.getTechnicalConditionENUM().equals(TechnicalConditionENUM.OK)) {
                personalInstrument.setIssueDate(null);
                personalInstrument.setPersonalConditionENUM(PersonalConditionENUM.NOT_ISSUED);
                personalInstrumentCondition.setIssueDate(null);
                personalInstrumentCondition.setPersonalConditionENUM(PersonalConditionENUM.NOT_ISSUED);
            }
            if (!personalInstrument.getPersonalConditionENUM().equals(PersonalConditionENUM.ISSUED) ||
                    !personalInstrument.getTechnicalConditionENUM().equals(TechnicalConditionENUM.OK)) {
                personalInstrument.setEmployee(null);
                personalInstrumentCondition.setEmployee(null);
            }
            try {
                personalInstrumentService.savePersonalInstrument(personalInstrument);
                personalInstrumentCondition.setPersonalInstrId(personalInstrument.getPersonalInstrId());
                personalInstrumentCondition.setTechnicalConditionENUM(personalInstrument.getTechnicalConditionENUM());
                personalInstrumentCondition.setPersonalConditionENUM(personalInstrument.getPersonalConditionENUM());
                personalInstrumentCondition.setBrokenDate(personalInstrument.getBrokenDate());
                personalInstrumentCondition.setStartRepairDate(personalInstrument.getStartRepairDate());
                personalInstrumentCondition.setEndRepairDate(personalInstrument.getEndRepairDate());
                personalInstrumentCondition.setIssueDate(personalInstrument.getIssueDate());
                personalInstrumentCondition.setEmployee(personalInstrument.getEmployee());
                personalInstrumentConditionService.savePersonalInstrument(personalInstrumentCondition);
                return "redirect:/general/p_instrument/p_instrumentUpdate/%d"
                        .formatted(personalInstrument.getPersonalInstrId());
            } catch (Exception e) {
                model.addAttribute("employeeList", employeeService.findAllActiveEmployees());
                model.addAttribute("conditionForTechn", TechnicalConditionENUM.values());
                if (employeeList.isEmpty()) {
                    model.addAttribute("conditionForPersonal", Arrays.stream(PersonalConditionENUM.values())
                            .filter(j -> j.equals(PersonalConditionENUM.NOT_ISSUED)).toList());
                } else {
                    model.addAttribute("conditionForPersonal", PersonalConditionENUM.values());
                }
                model.addAttribute("errorInv", UNIQUE_INSTR_INV);
                return "stock/p_instrument-update";
            }
        }
    }
}