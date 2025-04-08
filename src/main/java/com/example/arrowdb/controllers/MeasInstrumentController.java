package com.example.arrowdb.controllers;

import com.example.arrowdb.entity.*;
import com.example.arrowdb.enums.EmployeeStatusENUM;
import com.example.arrowdb.enums.TechnicalConditionENUM;
import com.example.arrowdb.enums.WorkConditionENUM;
import com.example.arrowdb.enums.WorkObjectStatusENUM;
import com.example.arrowdb.repositories.MeasInstrumentConditionAUDRepository;
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
public class MeasInstrumentController {

    private final EmployeeService employeeService;
    private final WorkObjectService workObjectService;
    private final MeasInstrumentService measInstrumentService;
    private final DepartmentService departmentService;
    private final MeasInstrumentConditionService measInstrumentConditionService;
    private final MeasInstrumentConditionAUDRepository measInstrumentConditionAUDRepository;

    @GetMapping("/general/m_instrument/catalog")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_STORE_MEAS_INSTR_VIEW')")
    public String getMeasInstrumentList(Model model) {
        model.addAttribute("measInstrument", measInstrumentService.findAllMeasInstruments().stream()
                .sorted(Comparator.comparingInt((MeasInstrument::getMeasInstrId)))
                .toList());
        model.addAttribute("technicalConditionENUM", TechnicalConditionENUM.values());
        model.addAttribute("workConditionENUM", WorkConditionENUM.values());
        model.addAttribute("departmentList", departmentService.findAllDepartmentsForSchedule());
        model.addAttribute("workObjectList", workObjectService.findAllWorkObjectForMainMenu());
        return "stock/m_instrument-catalog";
    }

    @GetMapping("/general/m_instrument")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_STORE_MEAS_INSTR_VIEW')")
    public String getPersonalInstrumentListEmployee(Model model) {
        model.addAttribute("employee", employeeService.findAllEmployeesForMeasInstrument());
        model.addAttribute("workObjectList", workObjectService.findAllWorkObjectForMainMenu());
        model.addAttribute("employeeStatusList", Arrays.stream(EmployeeStatusENUM.values())
                .filter(e -> !e.equals(EmployeeStatusENUM.DRAFT))
                .filter(e -> !e.equals(EmployeeStatusENUM.CLOSED))
                .toList());
        return "stock/m_instrument-com_table";
    }

    @GetMapping("/general/m_instrument/m_instrumentView/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_STORE_MEAS_INSTR_VIEW')")
    public String findMeasInstrumentById(@PathVariable("id") int id,
                                         Model model) {
        model.addAttribute("measInstrument", measInstrumentService.findMeasInstrumentById(id));
        model.addAttribute("measInstrumentConditionAUD", measInstrumentConditionAUDRepository
                .findAllMeasInstrumentConditionAUDById(id));
        return "stock/m_instrument-view";
    }

    @GetMapping("/general/m_instrument/m_instrumentCreate")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_STORE_MEAS_INSTR_CREATE')")
    public String createMeasInstrumentForm(@ModelAttribute MeasInstrument measInstrument,
                                           Model model) {
        try {
            model.addAttribute("departmentList", departmentService.findAllActiveDepartments());
        } catch (NullPointerException e) {
            e.getStackTrace();
        }
        return "stock/m_instrument-create";
    }

    @PostMapping("/general/m_instrument/m_instrumentCreate")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_STORE_MEAS_INSTR_CREATE')")
    public String createMeasInstrument(@Valid @ModelAttribute MeasInstrument measInstrument,
                                       BindingResult bindingResult,
                                       Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("departmentList", departmentService.findAllActiveDepartments());
            return "stock/m_instrument-create";
        } else {
            try {
                measInstrument.setTechnicalConditionENUM(TechnicalConditionENUM.OK);
                measInstrument.setWorkConditionENUM(WorkConditionENUM.NOT_INVOLVED);
                measInstrumentService.saveMeasInstrument(measInstrument);
                MeasInstrumentCondition measInstrumentCondition = new MeasInstrumentCondition();
                measInstrumentCondition.setTechnicalConditionENUM(TechnicalConditionENUM.OK);
                measInstrumentCondition.setWorkConditionENUM(WorkConditionENUM.NOT_INVOLVED);
                measInstrumentCondition.setMeasInstrId(measInstrument.getMeasInstrId());
                measInstrumentConditionService.saveMeasInstrumentCondition(measInstrumentCondition);
                return "redirect:/general/m_instrument/catalog";
            } catch (Exception e) {
                model.addAttribute("errorInv", UNIQUE_INSTR_INV);
                return "stock/m_instrument-create";
            }
        }
    }

    @GetMapping("/general/m_instrument/m_instrumentDelete/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_STORE_MEAS_INSTR_DELETE')")
    public String deleteMeasInstrument(@PathVariable("id") int id, Model model) {
        MeasInstrument measInstrument = measInstrumentService.findMeasInstrumentById(id);
        model.addAttribute("measInstrument", measInstrument);
        model.addAttribute("error", DELETE_INSTRUMENT_MESSAGE);
        if (measInstrument.getEmployee() != null || measInstrument.getWorkObject() != null) {
            return "error/m_instrument-error";
        } else {
            measInstrumentService.deleteMeasInstrumentById(id);
            return "redirect:/general/m_instrument/catalog";
        }
    }

    @GetMapping("/general/m_instrument/m_instrumentUpdate/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_STORE_MEAS_INSTR_UPDATE')")
    public String updateMeasInstrumentForm(@PathVariable("id") int id,
                                           Model model) {
        MeasInstrument measInstrument = measInstrumentService.findMeasInstrumentById(id);
        if (measInstrument.getTechnicalConditionENUM().equals(TechnicalConditionENUM.OUT)) {
            return "redirect:/general/m_instrument/m_instrumentView/%d"
                    .formatted(measInstrument.getMeasInstrId());
        }
        List<WorkObject> workObjectList = workObjectService.findAllWorkObjects().stream()
                .filter(e -> e.getWorkObjectStatusENUM().equals(WorkObjectStatusENUM.ACTIVE))
                .toList();
        List<Employee> employeeList = new ArrayList<>(employeeService.findAllActiveEmployees());
        if (!measInstrument.getTechnicalConditionENUM().equals(TechnicalConditionENUM.OK)) {
            measInstrument.setWorkConditionENUM(WorkConditionENUM.NOT_INVOLVED);
        }
        model.addAttribute("employeeList", employeeList);
        model.addAttribute("workObjectList", workObjectList);
        model.addAttribute("measInstrument", measInstrument);
        model.addAttribute("departmentList", departmentService.findAllActiveDepartments());
        if (employeeList.isEmpty() || workObjectList.isEmpty()) {
            model.addAttribute("conditionForWork", Arrays.stream(WorkConditionENUM.values())
                    .filter(e -> e.equals(WorkConditionENUM.NOT_INVOLVED))
                    .toList());
        } else {
            model.addAttribute("conditionForWork", WorkConditionENUM.values());
        }
        model.addAttribute("conditionForTechn", TechnicalConditionENUM.values());
        return "stock/m_instrument-update";
    }

    @PostMapping("/general/m_instrument/m_instrumentUpdate")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_STORE_MEAS_INSTR_UPDATE')")
    public String updateMeasInstrument(@Valid @ModelAttribute MeasInstrument measInstrument,
                                       BindingResult bindingResult,
                                       Model model) {
        List<WorkObject> workObjectList = new ArrayList<>(workObjectService.findAllWorkObjects().stream()
                .filter(e -> e.getWorkObjectStatusENUM().equals(WorkObjectStatusENUM.ACTIVE))
                .toList());
        List<Employee> employeeList = new ArrayList<>(employeeService.findAllActiveEmployees());
        MeasInstrumentCondition measInstrumentCondition = measInstrumentConditionService
                .findMeasInstrumentConditionById(measInstrument.getMeasInstrId());
        if (bindingResult.hasErrors()) {
            model.addAttribute("employeeList", employeeService.findAllActiveEmployees());
            model.addAttribute("workObjectList", workObjectService.findAllWorkObjects().stream()
                    .filter(e -> e.getWorkObjectStatusENUM().equals(WorkObjectStatusENUM.ACTIVE))
                    .toList());
            model.addAttribute("departmentList", departmentService.findAllActiveDepartments());
            model.addAttribute("conditionForTechn", TechnicalConditionENUM.values());
            if (employeeList.isEmpty() || workObjectList.isEmpty()) {
                model.addAttribute("conditionForWork", Arrays.stream(WorkConditionENUM.values())
                        .filter(e -> e.equals(WorkConditionENUM.NOT_INVOLVED))
                        .toList());
            } else {
                model.addAttribute("conditionForWork", WorkConditionENUM.values());
            }
            return "stock/m_instrument-update";
        } else {
            if (!measInstrument.getTechnicalConditionENUM().equals(TechnicalConditionENUM.OUT)) {
                measInstrument.setCloseDate(null);
            }
            if (!measInstrument.getWorkConditionENUM().equals(WorkConditionENUM.INVOLVED)) {
                measInstrument.setIssueDate(null);
                measInstrument.setWorkConditionENUM(WorkConditionENUM.NOT_INVOLVED);
            }
            if (!measInstrument.getWorkConditionENUM().equals(WorkConditionENUM.INVOLVED) ||
                !measInstrument.getTechnicalConditionENUM().equals(TechnicalConditionENUM.OK)) {
                measInstrument.setEmployee(null);
                measInstrument.setIssueDate(null);
                measInstrumentCondition.setEmployee(null);
                measInstrumentCondition.setIssueDate(null);
            }
            if (!measInstrument.getWorkConditionENUM().equals(WorkConditionENUM.INVOLVED) ||
                measInstrument.getTechnicalConditionENUM().equals(TechnicalConditionENUM.OUT)) {
                measInstrument.setWorkObject(null);
                measInstrument.setEmployee(null);
                measInstrument.setIssueDate(null);
                measInstrument.setWorkConditionENUM(WorkConditionENUM.NOT_INVOLVED);
                measInstrumentCondition.setWorkObject(null);
                measInstrumentCondition.setEmployee(null);
                measInstrumentCondition.setIssueDate(null);
                measInstrumentCondition.setWorkConditionENUM(WorkConditionENUM.NOT_INVOLVED);
            }
            if (measInstrument.getTechnicalConditionENUM().equals(TechnicalConditionENUM.OUT)) {
                measInstrument.setWorkObject(null);
                measInstrument.setEmployee(null);
                measInstrument.setIssueDate(null);
                measInstrument.setDepartment(null);
                measInstrument.setWorkConditionENUM(WorkConditionENUM.NOT_INVOLVED);
                measInstrumentCondition.setWorkObject(null);
                measInstrumentCondition.setEmployee(null);
                measInstrumentCondition.setIssueDate(null);
                measInstrumentCondition.setWorkConditionENUM(WorkConditionENUM.NOT_INVOLVED);
            }
            try {
                measInstrumentService.saveMeasInstrument(measInstrument);
                measInstrumentCondition.setMeasInstrId(measInstrument.getMeasInstrId());
                measInstrumentCondition.setTechnicalConditionENUM(measInstrument.getTechnicalConditionENUM());
                measInstrumentCondition.setWorkConditionENUM(measInstrument.getWorkConditionENUM());
                measInstrumentCondition.setBrokenDate(measInstrument.getBrokenDate());
                measInstrumentCondition.setStartRepairDate(measInstrument.getStartRepairDate());
                measInstrumentCondition.setEndRepairDate(measInstrument.getEndRepairDate());
                measInstrumentCondition.setIssueDate(measInstrument.getIssueDate());
                measInstrumentCondition.setEmployee(measInstrument.getEmployee());
                measInstrumentCondition.setWorkObject(measInstrument.getWorkObject());
                measInstrumentConditionService.saveMeasInstrumentCondition(measInstrumentCondition);
                return "redirect:/general/m_instrument/m_instrumentUpdate/%d"
                        .formatted(measInstrument.getMeasInstrId());
            } catch (Exception e) {
                model.addAttribute("employeeList", employeeService.findAllActiveEmployees());
                model.addAttribute("workObjectList", workObjectService.findAllWorkObjects().stream()
                        .filter(k -> k.getWorkObjectStatusENUM().getTitle().equals("Действующий"))
                        .toList());
                model.addAttribute("departmentList", departmentService.findAllActiveDepartments());
                model.addAttribute("conditionForTechn", TechnicalConditionENUM.values());
                if (employeeList.isEmpty() || workObjectList.isEmpty()) {
                    model.addAttribute("conditionForWork", Arrays.stream(WorkConditionENUM.values())
                            .filter(j -> j.equals(WorkConditionENUM.NOT_INVOLVED))
                            .toList());
                } else {
                    model.addAttribute("conditionForWork", WorkConditionENUM.values());
                }
                model.addAttribute("errorInv", UNIQUE_INSTR_INV);
                return "stock/m_instrument-update";
            }
        }
    }
}