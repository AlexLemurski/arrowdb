package com.example.arrowdb.controllers;

import com.example.arrowdb.entity.Department;
import com.example.arrowdb.entity.Employee;
import com.example.arrowdb.enums.EmployeeStatusENUM;
import com.example.arrowdb.enums.ProfAndDepStatusENUM;
import com.example.arrowdb.services.DepartmentService;
import com.example.arrowdb.services.EmployeeService;
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

import java.util.Comparator;
import java.util.List;

import static com.example.arrowdb.auxiliary.Message.*;

@Controller
@RequiredArgsConstructor
public class DepartmentController {

    private final DepartmentService departmentService;
    private final EmployeeService employeeService;

    @GetMapping("/general/department")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_DEPARTMENT_VIEW')")
    public String getDepartmentList(@NotNull Model model) {
        model.addAttribute("departmentList", departmentService.findAllDepartmentsForMainMenu());
        model.addAttribute("departmentStatusList", ProfAndDepStatusENUM.values());
        return "department/department-menu";
    }

    @GetMapping("/general/department/departmentView/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_DEPARTMENT_VIEW')")
    public String findDepartmentById(@PathVariable("id") int id,
                                     Model model) {
        model.addAttribute("department", departmentService.findDepartmentByIdForView(id));
        return "department/department-view";
    }

    @GetMapping("/general/department/departmentCreate")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_DEPARTMENT_CREATE')")
    public String createDepartmentForm(@ModelAttribute(value = "department") Department department) {
        return "department/department-create";
    }

    @PostMapping("/general/department/departmentCreate")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_DEPARTMENT_CREATE')")
    public String createDepartment(@Valid @ModelAttribute Department department,
                                   BindingResult bindingResult,
                                   Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("employeeList", employeeService.findAllActiveEmployees());
            return "department/department-create";
        } else {
            try {
                department.setProfAndDepStatusENUM(ProfAndDepStatusENUM.ACTIVE);
                departmentService.saveDepartment(department);
                return "redirect:/general/department";
            } catch (Exception e) {
                model.addAttribute("error", UNIQUE_DEPARTMENT);
                return "department/department-create";
            }
        }
    }

    @GetMapping("/general/department/departmentDelete/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_DEPARTMENT_DELETE')")
    public String deleteDepartment(@PathVariable("id") int id, Model model) {
        try {
            departmentService.deleteDepartment(id);
        } catch (Exception e) {
            model.addAttribute("department", departmentService.findDepartmentById(id));
            model.addAttribute("error", DELETE_DEPARTMENT_MESSAGE);
            return "error/department-error-delete";
        }
        return "redirect:/general/department";
    }

    @GetMapping("/general/department/departmentUpdate/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_DEPARTMENT_UPDATE')")
    public String updateDepartmentForm(@PathVariable("id") int id,
                                       Model model) {
        Department department = departmentService.findDepartmentById(id);
        if (department.getProfAndDepStatusENUM().equals(ProfAndDepStatusENUM.CLOSED)) {
            return "redirect:/general/department/departmentView/%d".formatted(department.getDepId());
        }
        model.addAttribute("departmentStatus", ProfAndDepStatusENUM.values());
        model.addAttribute("department", department);
        model.addAttribute("employeeList", employeeService.findAllActiveEmployees());
        return "department/department-update";
    }

    @PostMapping("/general/department/departmentUpdate")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_DEPARTMENT_UPDATE')")
    public String updatedepartment(@Valid @ModelAttribute Department department,
                                   BindingResult bindingResult,
                                   Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("departmentStatus", ProfAndDepStatusENUM.values());
            model.addAttribute("employeeList", employeeService.findAllEmployees().stream()
                    .filter(e -> e.getEmployeeStatusENUM().equals(EmployeeStatusENUM.ACTIVE))
                    .sorted(Comparator.comparingInt(Employee::getEmpId))
                    .toList());
            return "department/department-update";
        } else {
            Department departmentById = departmentService.findDepartmentById(department.getDepId());
            List<Employee> employeeActiveList = departmentById.getPersonalOfDepartment().stream()
                    .filter(e -> !e.getEmployeeStatusENUM().equals(EmployeeStatusENUM.CLOSED))
                    .toList();
            if (!employeeActiveList.isEmpty() && department.getProfAndDepStatusENUM()
                    .equals(ProfAndDepStatusENUM.CLOSED)) {
                model.addAttribute("employeeActiveList", employeeActiveList);
                model.addAttribute("department", departmentById);
                model.addAttribute("error", CHANGE_STATUS_DEPARTMENT_MESSAGE);
                return "error/department-error-update";
            }
            try {
                if (department.getProfAndDepStatusENUM().equals(ProfAndDepStatusENUM.CLOSED)) {
                    department.setDepartmentChief(null);
                }
                departmentService.saveDepartment(department);
                return "redirect:/general/department/departmentUpdate/%d".formatted(department.getDepId());
            } catch (Exception e) {
                model.addAttribute("error", UNIQUE_DEPARTMENT);
                return "department/department-update";
            }
        }
    }
}