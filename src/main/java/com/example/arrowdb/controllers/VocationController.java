package com.example.arrowdb.controllers;

import com.example.arrowdb.entity.Employee;
import com.example.arrowdb.entity.Vocation;
import com.example.arrowdb.enums.EmployeeStatusENUM;
import com.example.arrowdb.enums.VocationTypeENUM;
import com.example.arrowdb.services.DepartmentService;
import com.example.arrowdb.services.EmployeeService;
import com.example.arrowdb.services.ProfessionService;
import com.example.arrowdb.services.VocationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.time.LocalDate;
import java.util.Arrays;

@Controller
@RequiredArgsConstructor
public class VocationController {

    private final EmployeeService employeeService;
    private final VocationService vocationService;
    private final ProfessionService professionService;
    private final DepartmentService departmentService;

    @GetMapping("general/vocation")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_VOCATION_VIEW')")
    public String vocationList(@ModelAttribute Vocation vocation,
                               Model model) {
        model.addAttribute("professionList", professionService.findAllActiveProfessions());
        model.addAttribute("departmentList", departmentService.findAllActiveDepartments());
        model.addAttribute("employeeList", employeeService.findAllEmployeeForVocations());
        model.addAttribute("employeeStatusList", Arrays.stream(EmployeeStatusENUM.values())
                .toList().stream()
                .filter(e -> !e.equals(EmployeeStatusENUM.DRAFT))
                .filter(e -> !e.equals(EmployeeStatusENUM.CLOSED))
                .toList());
        model.addAttribute("vocationTypeList", VocationTypeENUM.values());
        return "vocation/vocation-menu";
    }

    @GetMapping("general/vocation/vocationUpdate/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_VOCATION_UPDATE')")
    public String vocationAddForm(@PathVariable("id") int id,
                                  @ModelAttribute Vocation vocation,
                                  Model model) {
        Employee employee = employeeService.findEmployeeById(id);
        if (employee.getEmployeeStatusENUM().equals(EmployeeStatusENUM.IN_VOCATION)){
            return "redirect:/general/vocation";
        }
        model.addAttribute("vocationType", VocationTypeENUM.values());
        model.addAttribute("vocationListByEmployee", vocationService.findByVocationsByEmployeeId(id));
        model.addAttribute("employee", employee);
        model.addAttribute("duringEmployeeList", employeeService.findAllActiveEmployees());
        model.addAttribute("currentDate", LocalDate.now());
        return "vocation/vocation-update";
    }

    @PostMapping("general/vocation/vocationUpdate")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_VOCATION_UPDATE')")
    public String vocationAdd(Vocation vocation,
                              Employee employee,
                              BindingResult bindingResult,
                              Model model) {
        if(bindingResult.hasErrors()) {
            model.addAttribute("vocationType", VocationTypeENUM.values());
            model.addAttribute("vocationListByEmployee",
                    vocationService.findByVocationsByEmployeeId(employee.getEmpId()));
            model.addAttribute("employee",
                    employeeService.findEmployeeById(employee.getEmpId()));
            model.addAttribute("duringEmployeeList",
                    employeeService.findAllEmployeeForMainMenu().stream());
            return "vocation/vocation-update";
        }
        vocation.setEndOfVocation(vocation.getStartOfVocation().plusDays(vocation.getDaysOfVocation()));
        vocationService.saveVocation(vocation);
        return "redirect:/general/vocation/vocationUpdate/%d".formatted(employee.getEmpId());
    }

    @GetMapping("general/vocation/vocationDelete/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_VOCATION_UPDATE')")
    public String vocationDelete(@PathVariable("id") int id) {
        int empId = vocationService.findEmployeeByVocationId(id);
        vocationService.deleteVocationById(id);
        return "redirect:/general/vocation/vocationUpdate/%d".formatted(empId);
    }
}