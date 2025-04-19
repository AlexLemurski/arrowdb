package com.example.arrowdb.controllers;

import com.example.arrowdb.enums.EmployeeStatusENUM;
import com.example.arrowdb.services.DepartmentService;
import com.example.arrowdb.services.ProfessionService;
import com.example.arrowdb.services.WorkObjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Arrays;


@Controller
@RequiredArgsConstructor
public class ScheduleController {

    private final DepartmentService departmentService;
    private final ProfessionService professionService;
    private final WorkObjectService workObjectService;

    @GetMapping("/general/schedule")
    public String getAllDepartment(Model model) {
        model.addAttribute("professionListSort", professionService.findAllActiveProfessions());
        model.addAttribute("departmentListSort", departmentService.findAllActiveDepartments());
        model.addAttribute("workObjectList", workObjectService.findAllActiveWorkObject());
        model.addAttribute("departmentList",
                departmentService.findAllDepartmentsForSchedule());
        model.addAttribute("employeeStatusList",
                Arrays.stream(EmployeeStatusENUM.values())
                        .filter(e -> !e.equals(EmployeeStatusENUM.DRAFT))
                        .filter(e -> !e.equals(EmployeeStatusENUM.CLOSED)));
        return "schedule/schedule-menu";
    }
}