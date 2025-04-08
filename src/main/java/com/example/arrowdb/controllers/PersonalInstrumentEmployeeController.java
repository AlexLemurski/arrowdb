package com.example.arrowdb.controllers;

import com.example.arrowdb.entity.Employee;
import com.example.arrowdb.entity.PersonalInstrument;
import com.example.arrowdb.entity.PersonalInstrumentCondition;
import com.example.arrowdb.enums.PersonalConditionENUM;
import com.example.arrowdb.services.EmployeeService;
import com.example.arrowdb.services.PersonalInstrumentConditionService;
import com.example.arrowdb.services.PersonalInstrumentService;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class PersonalInstrumentEmployeeController {

    private final EmployeeService employeeService;
    private final PersonalInstrumentService personalInstrumentService;
    private final PersonalInstrumentConditionService personalInstrumentConditionService;

    @GetMapping("/general/p_instrument/p_instrument-emp_update/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_STORE_PERS_INSTR_UPDATE')")
    public String updatePersonalInstrumentEmployee(@PathVariable("id") int id,
                                                   @ModelAttribute PersonalInstrument personalInstrument,
                                                   @NotNull Model model) {
        Employee employee = employeeService.findEmployeeById(id);
        List<PersonalInstrument> personalInstrumentList = personalInstrumentService
                .findAllActiveAndFreePersonalInstrument();
        model.addAttribute("personalInstrumentList", personalInstrumentList);
        model.addAttribute("employee", employee);
        return "stock/p_instrument-emp_update";
    }

    @GetMapping("/general/p_instrument/p_instrumentDelete_employee/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_STORE_PERS_INSTR_UPDATE')")
    public String deletePersonalInstrumentEmployee(@PathVariable("id") int id) {
        Integer localId = personalInstrumentService.findPersonalInstIdByEmployeeId(id);
        PersonalInstrument personalInstrument = personalInstrumentService
                .findPersonalInstrumentById(id);
        PersonalInstrumentCondition personalInstrumentCondition = personalInstrumentConditionService
                .findPersonalInstrumentById(id);
        personalInstrument.setPersonalConditionENUM(PersonalConditionENUM.NOT_ISSUED);
        personalInstrumentCondition.setPersonalConditionENUM(PersonalConditionENUM.NOT_ISSUED);
        personalInstrument.setEmployee(null);
        personalInstrumentCondition.setEmployee(null);
        personalInstrument.setIssueDate(null);
        personalInstrumentCondition.setIssueDate(null);
        personalInstrumentService.savePersonalInstrument(personalInstrument);
        personalInstrumentConditionService.savePersonalInstrument(personalInstrumentCondition);
        return "redirect:/general/p_instrument/p_instrument-emp_update/%d".formatted(localId);
    }

    @PostMapping("/general/p_instrument/p_instrumentCreate_employee/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_STORE_PERS_INSTR_UPDATE')")
    public String createSpecialClothEmployeeForm(@PathVariable("id") int id,
                                                 @ModelAttribute PersonalInstrument personalInstrument) {
        PersonalInstrumentCondition personalInstrumentCondition = personalInstrumentConditionService
                .findPersonalInstrumentById(personalInstrument.getPersonalInstrId());
        Employee employee = employeeService.findEmployeeById(id);
        personalInstrument.setPersonalConditionENUM(PersonalConditionENUM.ISSUED);
        personalInstrumentCondition.setPersonalConditionENUM(PersonalConditionENUM.ISSUED);
        personalInstrumentCondition.setIssueDate(personalInstrument.getIssueDate());
        personalInstrument.setEmployee(employee);
        personalInstrumentCondition.setEmployee(employee);
        personalInstrumentService.savePersonalInstrument(personalInstrument);
        personalInstrumentConditionService.savePersonalInstrument(personalInstrumentCondition);
        return "redirect:/general/p_instrument/p_instrument-emp_update/%d".formatted(id);
    }

}