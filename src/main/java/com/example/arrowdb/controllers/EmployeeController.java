package com.example.arrowdb.controllers;

import com.example.arrowdb.auxiliary.Auxiliary;
import com.example.arrowdb.entity.Employee;
import com.example.arrowdb.entity.Vocation;
import com.example.arrowdb.entity_service.EmployeeServiceEntity;
import com.example.arrowdb.enums.ClothSizeENUM;
import com.example.arrowdb.enums.DriverLicenseENUM;
import com.example.arrowdb.enums.EmployeeStatusENUM;
import com.example.arrowdb.services.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import static com.example.arrowdb.auxiliary.Message.CHANGE_STATUS_EMPLOYEE_MESSAGE;

@Controller
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;
    private final ProfessionService professionService;
    private final DepartmentService departmentService;
    private final UsersService usersService;
    private final VocationService vocationService;
    private final EmployeeServiceEntity employeeServiceEntity;
    private final Auxiliary auxiliary;

    @GetMapping("/general/employee")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_EMPLOYEE_VIEW')")
    public String getEmployeesList(@AuthenticationPrincipal UserDetails userDetails,
                                   Model model) {
        model.addAttribute("employeeList", employeeServiceEntity
                .employeeListForMainMenu(employeeService, userDetails));
        model.addAttribute("professionList", professionService.findAllActiveProfessions());
        model.addAttribute("departmentList", departmentService.findAllActiveDepartments());
        model.addAttribute("employeeStatusList", employeeServiceEntity
                .employeeMainMenuStatus(userDetails));
        return "employee/employee-menu";
    }

    @GetMapping("/general/employee/employeeView/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_EMPLOYEE_VIEW')")
    public String findEmployeeById(@PathVariable("id") int id,
                                   @AuthenticationPrincipal UserDetails userDetails,
                                   Model model) {
        Employee employee = employeeService.findEmployeeByIdForView(id);
        model.addAttribute("employee", employee);
        model.addAttribute("userName", userDetails.getUsername());
        model.addAttribute("accountName", employeeServiceEntity.getAccountOfEmployee(employee));
        for (Vocation vocation : vocationService.daysOfVocations(id)) {
            model.addAttribute("startOfVocation", vocation.getStartOfVocation());
            model.addAttribute("endOfVocation", vocation.getEndOfVocation());
            model.addAttribute("daysOfVocation", vocation.getDaysOfVocation());
            model.addAttribute("duringEmployee", vocation.getDuringEmployee());
        }
        model.addAttribute("adminAccept",
                userDetails.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN")));
        model.addAttribute("personalAccept",
                userDetails.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_EMPLOYEE_PERSONAL")));
        model.addAttribute("financeAccept",
                userDetails.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_EMPLOYEE_FINANCE")));
        model.addAttribute("createAccept",
                userDetails.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_EMPLOYEE_CREATE")));
        return "employee/employee-view";
    }

    @GetMapping("/general/employee/employeeCreate")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_EMPLOYEE_CREATE')")
    public String createEmployeeForm(@ModelAttribute(value = "employee") Employee employee,
                                     Model model) {
        model.addAttribute("professionList", professionService.findAllProfessionsForMainMenu());
        model.addAttribute("departmentList", departmentService.findAllDepartmentsForMainMenu());
        model.addAttribute("employeeStatusList", employeeServiceEntity.employeeCreateStatus());
        return "employee/employee-create";
    }

    @PostMapping("/general/employee/employeeCreate")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_EMPLOYEE_CREATE')")
    public String createEmployee(@Valid Employee employee,
                                 BindingResult bindingResult,
                                 Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("professionList", professionService.findAllProfessionsForMainMenu());
            model.addAttribute("departmentList", departmentService.findAllDepartmentsForMainMenu());
            model.addAttribute("employeeStatusList", employeeServiceEntity.employeeCreateStatus());
            return "employee/employee-create";
        } else {
            employee.setClothSizeENUM(null);
            employeeService.saveEmployee(employee);
            return "redirect:/general/employee";
        }
    }

    @GetMapping("/general/employee/employeeDelete/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_EMPLOYEE_DELETE')")
    public String deleteEmployee(@PathVariable("id") int id,
                                 Model model) {
        try {
            employeeService.deleteEmployeeById(id);
        } catch (Exception e) {
            model.addAttribute("employee", employeeService.findEmployeeById(id));
            model.addAttribute("error", CHANGE_STATUS_EMPLOYEE_MESSAGE);
            return "error/employee-error";
        }
        return "redirect:/general/employee";
    }

    @GetMapping("/general/employee/employeeUpdate/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_EMPLOYEE_UPDATE')")
    public String updateEmployeeForm(@PathVariable("id") int id,
                                     Model model) {
        UserDetails userDetails = auxiliary.getUser();
        Employee employee = employeeService.findEmployeeById(id);
        if (employee.getEmployeeStatusENUM().equals(EmployeeStatusENUM.IN_BUSINESS_TRIP) ||
            employee.getEmployeeStatusENUM().equals(EmployeeStatusENUM.IN_VOCATION) ||
            employee.getEmployeeStatusENUM().equals(EmployeeStatusENUM.ON_SICK_LEAVE) ||
            employee.getEmployeeStatusENUM().equals(EmployeeStatusENUM.CLOSED)) {
            return "redirect:/general/employee/employeeView/%d".formatted(employee.getEmpId());
        }
        model.addAttribute("employee", employee);
        model.addAttribute("professionList", professionService.findAllProfessionsForMainMenu());
        model.addAttribute("departmentList", departmentService.findAllDepartmentsForMainMenu());
        model.addAttribute("driverLicenseList", DriverLicenseENUM.values());
        model.addAttribute("employeeStatus", employeeServiceEntity.employeeUpdateStatus(employee));
        model.addAttribute("clothSize", ClothSizeENUM.values());
        model.addAttribute("adminAccept",
                userDetails.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN")));
        model.addAttribute("personalAccept",
                userDetails.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_EMPLOYEE_PERSONAL")));
        model.addAttribute("financeAccept",
                userDetails.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_EMPLOYEE_FINANCE")));
        return "employee/employee-update";
    }

    @PostMapping("/general/employee/employeeUpdate")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_EMPLOYEE_UPDATE')")
    public String updateEmployee(@Valid @ModelAttribute Employee employee,
                                 BindingResult bindingResult,
                                 Model model) {
        if (bindingResult.hasErrors()) {
            UserDetails userDetails = auxiliary.getUser();
            model.addAttribute("professionList", professionService.findAllProfessionsForMainMenu());
            model.addAttribute("departmentList", departmentService.findAllDepartmentsForMainMenu());
            model.addAttribute("driverLicenseList", DriverLicenseENUM.values());
            model.addAttribute("employeeStatus", employeeServiceEntity.employeeUpdateStatus(employee));
            model.addAttribute("clothSize", ClothSizeENUM.values());
            model.addAttribute("adminAccept",
                    userDetails.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN")));
            model.addAttribute("personalAccept",
                    userDetails.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_EMPLOYEE_PERSONAL")));
            model.addAttribute("financeAccept",
                    userDetails.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_EMPLOYEE_FINANCE")));
            return "employee/employee-update";
        } else {
            Employee employeeById = employeeService.findEmployeeByIdForView(employee.getEmpId());
            if (employeeServiceEntity.checkEmployeeRelationshipsBeforeClose(employeeById, employee)) {
                model.addAttribute("employee", employeeById);
                model.addAttribute("error", CHANGE_STATUS_EMPLOYEE_MESSAGE);
                return "error/employee-error";
            } else {
                employeeServiceEntity.offUserStatusAfterCloseEmployee(employee, usersService);
                employeeService.saveEmployee(employee);
            }
        }
        return "redirect:/general/employee/employeeUpdate/%d".formatted(employee.getEmpId());
    }

}