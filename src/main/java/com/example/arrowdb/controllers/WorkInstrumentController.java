package com.example.arrowdb.controllers;

import com.example.arrowdb.entity.*;
import com.example.arrowdb.enums.*;
import com.example.arrowdb.repositories.WorkInstrumentConditionAUDRepository;
import com.example.arrowdb.services.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
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

import static com.example.arrowdb.auxiliary.Message.DELETE_INSTRUMENT_MESSAGE;
import static com.example.arrowdb.auxiliary.Message.UNIQUE_INSTR_INV;

@Controller
@RequiredArgsConstructor
public class WorkInstrumentController {

    private final EmployeeService employeeService;
    private final WorkObjectService workObjectService;
    private final DepartmentService departmentService;
    private final WorkInstrumentService workInstrumentService;
    private final WorkInstrumentConditionService workInstrumentConditionService;
    private final WorkInstrumentConditionAUDRepository workInstrumentConditionAUDRepository;

    @GetMapping("/general/w_instrument/catalog")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_STORE_WORK_INSTR_VIEW')")
    public String getWorkInstrumentList(Model model) {
        model.addAttribute("workInstrument", workInstrumentService.findAllWorkInstruments().stream()
                .sorted(Comparator.comparingInt((WorkInstrument::getWorkInstrId)))
                .toList());
        model.addAttribute("technicalConditionENUM", TechnicalConditionENUM.values());
        model.addAttribute("workConditionENUM", WorkConditionENUM.values());
        model.addAttribute("departmentList", departmentService.findAllDepartmentsForSchedule());
        model.addAttribute("workObjectList", workObjectService.findAllWorkObjectForMainMenu());
        return "stock/w_instrument-catalog";
    }

    @GetMapping("/general/w_instrument")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_STORE_WORK_INSTR_VIEW')")
    public String getPersonalInstrumentListEmployee(Model model) {
        model.addAttribute("employee", employeeService.findAllEmployeesForWorkInstrument());
        model.addAttribute("workObjectList", workObjectService.findAllWorkObjectForMainMenu());
        model.addAttribute("employeeStatusList", Arrays.stream(EmployeeStatusENUM.values())
                .filter(e -> !e.equals(EmployeeStatusENUM.DRAFT))
                .filter(e -> !e.equals(EmployeeStatusENUM.CLOSED))
                .toList());
        return "stock/w_instrument-com_table";
    }

    @GetMapping("/general/w_instrument/w_instrumentView/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_STORE_WORK_INSTR_VIEW')")
    public String findWorkInstrumentById(@PathVariable("id") int id,
                                         Model model) {
        model.addAttribute("workInstrument", workInstrumentService.findWorkInstrumentById(id));
        model.addAttribute("workInstrumentConditionAUD", workInstrumentConditionAUDRepository
                .findAllWorkInstrumentConditionAUDById(id));
        return "stock/w_instrument-view";
    }

    @GetMapping("/general/w_instrument/w_instrumentCreate")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_STORE_WORK_INSTR_CREATE')")
    public String createWorkInstrumentForm(@ModelAttribute WorkInstrument workInstrument,
                                           Model model) {
        try {
            model.addAttribute("departmentList", departmentService.findAllActiveDepartments());
        } catch (NullPointerException e) {
            e.getStackTrace();
        }
        return "stock/w_instrument-create";
    }

    @PostMapping("/general/w_instrument/w_instrumentCreate")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_STORE_WORK_INSTR_CREATE')")
    public String createWorkInstrument(@Valid @ModelAttribute WorkInstrument workInstrument,
                                       BindingResult bindingResult,
                                       Model model) {
        model.addAttribute("departmentList", departmentService.findAllActiveDepartments());
        if (bindingResult.hasErrors()) {
            return "stock/w_instrument-create";
        } else {
            try {
                workInstrument.setTechnicalConditionENUM(TechnicalConditionENUM.OK);
                workInstrument.setWorkConditionENUM(WorkConditionENUM.NOT_INVOLVED);
                workInstrumentService.saveWorkInstrument(workInstrument);
                WorkInstrumentCondition workInstrumentCondition = new WorkInstrumentCondition();
                workInstrumentCondition.setTechnicalConditionENUM(TechnicalConditionENUM.OK);
                workInstrumentCondition.setWorkConditionENUM(WorkConditionENUM.NOT_INVOLVED);
                workInstrumentCondition.setWorkInstrId(workInstrument.getWorkInstrId());
                workInstrumentConditionService.saveWorkInstrumentCondition(workInstrumentCondition);
                return "redirect:/general/w_instrument/catalog";
            } catch (Exception e) {
                model.addAttribute("errorInv", UNIQUE_INSTR_INV);
                return "stock/w_instrument-create";
            }
        }
    }

    @GetMapping("/general/w_instrument/w_instrumentDelete/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_STORE_WORK_INSTR_DELETE')")
    public String deleteWorkInstrument(@PathVariable("id") int id,
                                       Model model) {
        WorkInstrument workInstrument = workInstrumentService.findWorkInstrumentById(id);
        model.addAttribute("workInstrument", workInstrument);
        model.addAttribute("error", DELETE_INSTRUMENT_MESSAGE);
        if (workInstrument.getEmployee() != null || workInstrument.getWorkObject() != null) {
            return "error/w_instrument-error";
        } else {
            workInstrumentService.deleteWorkInstrumentsById(id);
            workInstrumentConditionService.deleteWorkInstrumentConditionById(id);
            return "redirect:/general/w_instrument/catalog";
        }
    }

    @GetMapping("/general/w_instrument/w_instrumentUpdate/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_STORE_WORK_INSTR_UPDATE')")
    public String updateWorkInstrumentForm(@PathVariable("id") int id,
                                           Model model) {
        WorkInstrument workInstrument = workInstrumentService.findWorkInstrumentById(id);
        if (workInstrument.getTechnicalConditionENUM().equals(TechnicalConditionENUM.OUT)) {
            return "redirect:/general/w_instrument/w_instrumentView/%d"
                    .formatted(workInstrument.getWorkInstrId());
        }
        List<WorkObject> workObjectList = new ArrayList<>(workObjectService.findAllWorkObjects().stream()
                .filter(e -> e.getWorkObjectStatusENUM().equals(WorkObjectStatusENUM.ACTIVE))
                .toList());
        List<Employee> employeeList = new ArrayList<>(employeeService.findAllActiveEmployees());
        if (!workInstrument.getTechnicalConditionENUM().equals(TechnicalConditionENUM.OK)) {
            workInstrument.setWorkConditionENUM(WorkConditionENUM.NOT_INVOLVED);
        }
        model.addAttribute("employeeList", employeeList);
        model.addAttribute("workInstrument", workInstrument);
        model.addAttribute("workObjectList", workObjectList);
        model.addAttribute("departmentList", departmentService.findAllActiveDepartments());
        if (employeeList.isEmpty() || workObjectList.isEmpty()) {
            model.addAttribute("conditionForWork", Arrays.stream(WorkConditionENUM.values())
                    .filter(e -> e.equals(PersonalConditionENUM.NOT_ISSUED))
                    .toList());
        } else {
            model.addAttribute("conditionForWork", WorkConditionENUM.values());
        }
        model.addAttribute("conditionForTechn", TechnicalConditionENUM.values());
        return "stock/w_instrument-update";
    }

    @PostMapping("/general/w_instrument/w_instrumentUpdate")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_STORE_WORK_INSTR_UPDATE')")
    public String updateWorkInstrument(@Valid @ModelAttribute WorkInstrument workInstrument,
                                       BindingResult bindingResult,
                                       Model model) {
        List<WorkObject> workObjectList = workObjectService.findAllWorkObjects().stream()
                .filter(e -> e.getWorkObjectStatusENUM().equals(WorkObjectStatusENUM.ACTIVE))
                .toList();
        List<Employee> employeeList = employeeService.findAllActiveEmployees();
        WorkInstrumentCondition workInstrumentCondition = workInstrumentConditionService
                .findWorkInstrumentConditionById(workInstrument.getWorkInstrId());
        if (bindingResult.hasErrors()) {
            model.addAttribute("employeeList", employeeList);
            model.addAttribute("workObjectList", workObjectList);
            model.addAttribute("departmentList", departmentService.findAllActiveDepartments());
            model.addAttribute("conditionForTechn", TechnicalConditionENUM.values());
            if (employeeList.isEmpty() || workObjectList.isEmpty()) {
                model.addAttribute("conditionForWork", Arrays.stream(WorkConditionENUM.values())
                        .filter(e -> e.equals(WorkConditionENUM.NOT_INVOLVED))
                        .toList());
            } else {
                model.addAttribute("conditionForWork", WorkConditionENUM.values());
            }
            return "stock/w_instrument-update";
        } else {
            if (!workInstrument.getTechnicalConditionENUM().equals(TechnicalConditionENUM.OUT)) {
                workInstrument.setCloseDate(null);
            }
            if (!workInstrument.getWorkConditionENUM().equals(WorkConditionENUM.INVOLVED)) {
                workInstrument.setIssueDate(null);
                workInstrument.setWorkConditionENUM(WorkConditionENUM.NOT_INVOLVED);
            }
            if (!workInstrument.getWorkConditionENUM().equals(WorkConditionENUM.INVOLVED) ||
                !workInstrument.getTechnicalConditionENUM().equals(TechnicalConditionENUM.OK)) {
                workInstrument.setEmployee(null);
                workInstrument.setIssueDate(null);
                workInstrumentCondition.setEmployee(null);
                workInstrumentCondition.setIssueDate(null);
            }
            if (!workInstrument.getWorkConditionENUM().equals(WorkConditionENUM.INVOLVED) ||
                workInstrument.getTechnicalConditionENUM().equals(TechnicalConditionENUM.OUT)) {
                workInstrument.setWorkObject(null);
                workInstrument.setEmployee(null);
                workInstrument.setIssueDate(null);
                workInstrument.setWorkConditionENUM(WorkConditionENUM.NOT_INVOLVED);
                workInstrumentCondition.setWorkObject(null);
                workInstrumentCondition.setEmployee(null);
                workInstrumentCondition.setIssueDate(null);
                workInstrumentCondition.setWorkConditionENUM(WorkConditionENUM.NOT_INVOLVED);
            }
            if (workInstrument.getTechnicalConditionENUM().equals(TechnicalConditionENUM.OUT)) {
                workInstrument.setWorkObject(null);
                workInstrument.setEmployee(null);
                workInstrument.setIssueDate(null);
                workInstrument.setDepartment(null);
                workInstrument.setWorkConditionENUM(WorkConditionENUM.NOT_INVOLVED);
                workInstrumentCondition.setWorkObject(null);
                workInstrumentCondition.setEmployee(null);
                workInstrumentCondition.setIssueDate(null);
                workInstrumentCondition.setWorkConditionENUM(WorkConditionENUM.NOT_INVOLVED);
            }
            try {
                workInstrumentService.saveWorkInstrument(workInstrument);
                workInstrumentCondition.setWorkInstrId(workInstrument.getWorkInstrId());
                workInstrumentCondition.setTechnicalConditionENUM(workInstrument.getTechnicalConditionENUM());
                workInstrumentCondition.setWorkConditionENUM(workInstrument.getWorkConditionENUM());
                workInstrumentCondition.setBrokenDate(workInstrument.getBrokenDate());
                workInstrumentCondition.setStartRepairDate(workInstrument.getStartRepairDate());
                workInstrumentCondition.setEndRepairDate(workInstrument.getEndRepairDate());
                workInstrumentCondition.setIssueDate(workInstrument.getIssueDate());
                workInstrumentCondition.setEmployee(workInstrument.getEmployee());
                workInstrumentCondition.setWorkObject(workInstrument.getWorkObject());
                workInstrumentConditionService.saveWorkInstrumentCondition(workInstrumentCondition);
                return "redirect:/general/w_instrument/w_instrumentUpdate/%d"
                        .formatted(workInstrument.getWorkInstrId());
            } catch (Exception e) {
                model.addAttribute("employeeList", employeeService.findAllActiveEmployees());
                model.addAttribute("workObjectList", workObjectService.findAllWorkObjects().stream()
                        .filter(k -> k.getWorkObjectStatusENUM().equals(WorkObjectStatusENUM.ACTIVE))
                        .toList());
                model.addAttribute("departmentList", departmentService.findAllActiveDepartments());
                model.addAttribute("conditionForTechn", TechnicalConditionENUM.values());
                if (employeeList.isEmpty() || workObjectList.isEmpty()) {
                    model.addAttribute("conditionForWork", Arrays.stream(WorkConditionENUM.values())
                            .filter(j -> j.equals(WorkConditionENUM.INVOLVED))
                            .toList());
                } else {
                    model.addAttribute("conditionForWork", WorkConditionENUM.values());
                }
                model.addAttribute("errorInv", UNIQUE_INSTR_INV);
                return "stock/w_instrument-update";
            }
        }
    }
}