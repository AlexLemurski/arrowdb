package com.example.arrowdb.controllers;

import com.example.arrowdb.enums.EmployeeStatusENUM;
import com.example.arrowdb.services.DepartmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Arrays;


@Controller
@RequiredArgsConstructor
public class ScheduleController {

    private final DepartmentService departmentService;

    @GetMapping("/general/schedule")
    public String getAllDepartment(Model model) {
        model.addAttribute("departmentList",
                departmentService.findAllDepartmentsForSchedule());
        model.addAttribute("employeeStatusList",
                Arrays.stream(EmployeeStatusENUM.values())
                        .filter(e -> !e.equals(EmployeeStatusENUM.CLOSED)));
        return "schedule/schedule-menu";
    }
}