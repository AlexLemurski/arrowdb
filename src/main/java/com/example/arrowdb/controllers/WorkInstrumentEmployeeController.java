package com.example.arrowdb.controllers;

import com.example.arrowdb.auxiliary.TempIssueDate;
import com.example.arrowdb.entity.Employee;
import com.example.arrowdb.entity.WorkInstrument;
import com.example.arrowdb.entity.WorkInstrumentCondition;
import com.example.arrowdb.entity.WorkObject;
import com.example.arrowdb.enums.TechnicalConditionENUM;
import com.example.arrowdb.enums.WorkConditionENUM;
import com.example.arrowdb.enums.WorkObjectStatusENUM;
import com.example.arrowdb.services.EmployeeService;
import com.example.arrowdb.services.WorkInstrumentConditionService;
import com.example.arrowdb.services.WorkInstrumentService;
import com.example.arrowdb.services.WorkObjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Controller
@RequiredArgsConstructor
public class WorkInstrumentEmployeeController {

    private final EmployeeService employeeService;
    private final WorkObjectService workObjectService;
    private final WorkInstrumentService workInstrumentService;
    private final WorkInstrumentConditionService workInstrumentConditionService;


    @GetMapping("/general/w_instrument/w_instrument-emp_update/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_STORE_WORK_INSTR_UPDATE')")
    public String updatePersonalInstrumentEmployee(@PathVariable("id") int id,
                                                   @ModelAttribute WorkInstrument workInstrument,
                                                   @ModelAttribute TempIssueDate tempIssueDate,
                                                   Model model) {
        Employee employee = employeeService.findEmployeeById(id);
        List<Employee> employeeList = new ArrayList<>(employeeService.findAllActiveEmployees());
        employeeList.remove(employee);
        List<WorkInstrument> workInstrumentList = workInstrumentService
                .findAllWorkInstruments().stream()
                .filter(e -> e.getTechnicalConditionENUM().equals(TechnicalConditionENUM.OK))
                .filter(e -> e.getWorkConditionENUM().equals(WorkConditionENUM.NOT_INVOLVED))
                .toList();
        List<WorkObject> workObjectList = workObjectService.findAllWorkObjects().stream()
                .filter(e -> e.getWorkObjectStatusENUM().equals(WorkObjectStatusENUM.ACTIVE))
                .toList();
        model.addAttribute("workInstrumentList", workInstrumentList);
        model.addAttribute("workObjectList", workObjectList);
        model.addAttribute("employee", employee);
        model.addAttribute("employeeList", employeeList);
        return "stock/w_instrument-emp_update";
    }

    @PostMapping("/general/w_instrument/w_instrumentCreate_employee/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_STORE_WORK_INSTR_UPDATE')")
    public String createPersonalInstrumentEmployeeForm(@PathVariable("id") int id,
                                                       @RequestParam
                                                       @ModelAttribute List<WorkInstrument> workInstrumentListAdd,
                                                       @ModelAttribute WorkObject workObject,
                                                       TempIssueDate tempIssueDate) {
        Employee employee = employeeService.findEmployeeById(id);
        for (WorkInstrument workInstrument : workInstrumentListAdd) {
            workInstrument.setWorkConditionENUM(WorkConditionENUM.INVOLVED);
            workInstrument.setEmployee(employee);
            workInstrument.setWorkObject(workObject);
            workInstrument.setIssueDate(tempIssueDate.getTIssueDate());
            workInstrumentService.saveWorkInstrument(workInstrument);
            WorkInstrumentCondition workInstrumentCondition = workInstrumentConditionService
                    .findWorkInstrumentConditionById(workInstrument.getWorkInstrId());
            workInstrumentCondition.setWorkConditionENUM(WorkConditionENUM.INVOLVED);
            workInstrumentCondition.setWorkObject(workObject);
            workInstrumentCondition.setEmployee(employee);
            workInstrumentCondition.setIssueDate(tempIssueDate.getTIssueDate());
            workInstrumentConditionService.saveWorkInstrumentCondition(workInstrumentCondition);
        }
        return "redirect:/general/w_instrument/w_instrument-emp_update/%d".formatted(id);
    }

    @GetMapping("/general/w_instrument/w_instrumentDelete_employee/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_STORE_WORK_INSTR_UPDATE')")
    public String deletePersonalInstrumentEmployee(@PathVariable("id") int id) {
        Integer localId = workInstrumentService.findEmployeeIdByWorkInstId(id);
        WorkInstrument workInstrument = workInstrumentService.findWorkInstrumentById(id);
        WorkInstrumentCondition workInstrumentCondition =
                workInstrumentConditionService.findWorkInstrumentConditionById(id);
        workInstrument.setWorkConditionENUM(WorkConditionENUM.NOT_INVOLVED);
        workInstrument.setWorkObject(null);
        workInstrument.setEmployee(null);
        workInstrument.setIssueDate(null);
        workInstrumentService.saveWorkInstrument(workInstrument);
        workInstrumentCondition.setWorkConditionENUM(WorkConditionENUM.NOT_INVOLVED);
        workInstrumentCondition.setWorkObject(null);
        workInstrumentCondition.setEmployee(null);
        workInstrumentCondition.setIssueDate(null);
        workInstrumentConditionService.saveWorkInstrumentCondition(workInstrumentCondition);
        return "redirect:/general/w_instrument/w_instrument-emp_update/%d".formatted(localId);
    }

    @GetMapping("/general/w_instrument/w_instrumentDeleteAll_employee/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_STORE_WORK_INSTR_UPDATE')")
    public String deleteAllPersonalInstrumentEmployee(@PathVariable("id") int id) {
        Employee employee = employeeService.findEmployeeById(id);
        Set<WorkInstrument> workInstrumentListCurrent = employee.getWorkInstrumentList();
        for (WorkInstrument workInstrument : workInstrumentListCurrent) {
            workInstrument.setWorkConditionENUM(WorkConditionENUM.NOT_INVOLVED);
            workInstrument.setWorkObject(null);
            workInstrument.setEmployee(null);
            workInstrument.setIssueDate(null);
            workInstrumentService.saveWorkInstrument(workInstrument);
            WorkInstrumentCondition workInstrumentCondition = workInstrumentConditionService
                    .findWorkInstrumentConditionById(workInstrument.getWorkInstrId());
            workInstrumentCondition.setWorkConditionENUM(WorkConditionENUM.NOT_INVOLVED);
            workInstrumentCondition.setWorkObject(null);
            workInstrumentCondition.setEmployee(null);
            workInstrumentCondition.setIssueDate(null);
            workInstrumentConditionService.saveWorkInstrumentCondition(workInstrumentCondition);
        }

        return "redirect:/general/w_instrument/w_instrument-emp_update/%d".formatted(id);
    }

    @PostMapping("/general/w_instrument/w_instrumentChange_employee/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_STORE_WORK_INSTR_UPDATE')")
    public String changeAllSpecialClothEmployeeForm(@PathVariable("id") int id,
                                                    Employee employee,
                                                    WorkObject workObject,
                                                    TempIssueDate tempIssueDate) {
        Integer localId = workInstrumentService.findEmployeeIdByWorkInstId(id);
        WorkInstrument workInstrument = workInstrumentService.findWorkInstrumentById(id);
        workInstrument.setWorkObject(workObject);
        workInstrument.setEmployee(employee);
        workInstrument.setIssueDate(tempIssueDate.getTIssueDate());
        workInstrumentService.saveWorkInstrument(workInstrument);
        WorkInstrumentCondition workInstrumentCondition = workInstrumentConditionService
                .findWorkInstrumentConditionById(workInstrument.getWorkInstrId());
        workInstrumentCondition.setWorkObject(workObject);
        workInstrumentCondition.setEmployee(employee);
        workInstrumentCondition.setIssueDate(tempIssueDate.getTIssueDate());
        workInstrumentConditionService.saveWorkInstrumentCondition(workInstrumentCondition);
        return "redirect:/general/w_instrument/w_instrument-emp_update/%d".formatted(localId);
    }

    @PostMapping("/general/w_instrument/w_instrumentChangeAll_employee/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_STORE_WORK_INSTR_UPDATE')")
    public String changeSpecialClothEmployeeForm(@PathVariable("id") int id,
                                                 Employee employee,
                                                 WorkObject workObject,
                                                 TempIssueDate tempIssueDate) {
        Employee employeeById = employeeService.findEmployeeById(id);
        Set<WorkInstrument> workInstrumentListCurrent = employeeById.getWorkInstrumentList();
        for (WorkInstrument workInstrument : workInstrumentListCurrent) {
            workInstrument.setEmployee(employee);
            workInstrument.setWorkObject(workObject);
            workInstrument.setIssueDate(tempIssueDate.getTIssueDate());
            workInstrumentService.saveWorkInstrument(workInstrument);
            WorkInstrumentCondition workInstrumentCondition = workInstrumentConditionService
                    .findWorkInstrumentConditionById(workInstrument.getWorkInstrId());
            workInstrumentCondition.setWorkObject(workObject);
            workInstrumentCondition.setEmployee(employee);
            workInstrumentCondition.setIssueDate(tempIssueDate.getTIssueDate());
            workInstrumentConditionService.saveWorkInstrumentCondition(workInstrumentCondition);
        }
        return "redirect:/general/w_instrument/w_instrument-emp_update/%d".formatted(id);
    }
}