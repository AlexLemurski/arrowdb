package com.example.arrowdb.controllers;

import com.example.arrowdb.auxiliary.MailSenderService;
import com.example.arrowdb.entity.Employee;
import com.example.arrowdb.entity.Roles;
import com.example.arrowdb.entity.Users;
import com.example.arrowdb.enums.EmployeeStatusENUM;
import com.example.arrowdb.enums.UserStatusENUM;
import com.example.arrowdb.services.EmployeeService;
import com.example.arrowdb.services.RoleService;
import com.example.arrowdb.services.UsersService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Arrays;
import java.util.Comparator;

import static com.example.arrowdb.auxiliary.Message.ERROR_CREATE_NEW_USER;
import static com.example.arrowdb.auxiliary.PassGenerator.randomPass;

@Controller
@RequiredArgsConstructor
public class UsersController {

    private final UsersService usersService;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;
    private final EmployeeService employeeService;
    private final MailSenderService mailSenderService;

    @GetMapping("/general/users")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public String getAllUsers(Model model) {
        model.addAttribute("users", usersService.findAllUsers());
        model.addAttribute("userStatusList", UserStatusENUM.values());
        return "user/user-menu";
    }

    @GetMapping("/general/users/userCreate")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public String createUserForm(@ModelAttribute(value = "users") Users users,
                                 Model model) {
        model.addAttribute("employeeList", employeeService.findEmployeeForCreateAccount());
        model.addAttribute("employee", roleService.findRolesByMenuName("employee"));
        model.addAttribute("profession", roleService.findRolesByMenuName("profession"));
        model.addAttribute("department", roleService.findRolesByMenuName("department"));
        model.addAttribute("schedule", roleService.findRolesByMenuName("schedule"));
        model.addAttribute("vocation", roleService.findRolesByMenuName("vocation"));
        model.addAttribute("store_personal", roleService.findRolesByMenuName("store_personal"));
        model.addAttribute("store_work", roleService.findRolesByMenuName("store_work"));
        model.addAttribute("store_meas", roleService.findRolesByMenuName("store_meas"));
        model.addAttribute("store_scloth", roleService.findRolesByMenuName("store_scloth"));
        model.addAttribute("activity_work", roleService.findRolesByMenuName("activity_work"));
        model.addAttribute("activity_control", roleService.findRolesByMenuName("activity_control"));
        model.addAttribute("activity_doc", roleService.findRolesByMenuName("activity_doc"));
        model.addAttribute("perspective_doc", roleService.findRolesByMenuName("perspective_doc"));
        model.addAttribute("statusList", UserStatusENUM.values());
        return "user/user-create";
    }

    @PostMapping("/general/users/userCreate")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public String createUser(@Valid @ModelAttribute Users users,
//                             @ModelAttribute Employee employee,
                             Model model) {
        String login = users.getEmployee().getName().substring(0, 1).toUpperCase()
                + users.getEmployee().getMiddleName().substring(0, 1).toUpperCase() + "_"
                + users.getEmployee().getSurName() + "_" + users.getEmployee().getEmpId();
        users.setUserName(login);
        users.getEmployee().setAccount(users);
        String tempPassword = randomPass();
        users.setPassword(tempPassword);
        users.setPassword(passwordEncoder.encode(users.getPassword()));
        usersService.createUser(users);
        try {
            mailSenderService.send(
                    users.getEmployee().getEmail(),
                    "Создание данных для входа в систему",
                    String.format("Логин: %s%nПароль: %s%nСтатус учетной записи: " +
                                    "%s%nАдрес: http://@@@/login",
                            users.getUserName(),
                            tempPassword,
                            users.getUserStatusENUM().getTitle()));
        } catch (Exception e) {
            users.setPassword(null);
            users.getEmployee().setAccount(null);
            usersService.deleteUser(users.getUserId());
            model.addAttribute("employeeList", employeeService.findEmployeeForCreateAccount());
            model.addAttribute("employee", roleService.findRolesByMenuName("employee"));
            model.addAttribute("profession", roleService.findRolesByMenuName("profession"));
            model.addAttribute("department", roleService.findRolesByMenuName("department"));
            model.addAttribute("schedule", roleService.findRolesByMenuName("schedule"));
            model.addAttribute("vocation", roleService.findRolesByMenuName("vocation"));
            model.addAttribute("store_personal", roleService.findRolesByMenuName("store_personal"));
            model.addAttribute("store_work", roleService.findRolesByMenuName("store_work"));
            model.addAttribute("store_meas", roleService.findRolesByMenuName("store_meas"));
            model.addAttribute("store_scloth", roleService.findRolesByMenuName("store_scloth"));
            model.addAttribute("activity_work", roleService.findRolesByMenuName("activity_work"));
            model.addAttribute("activity_control", roleService.findRolesByMenuName("activity_control"));
            model.addAttribute("activity_doc", roleService.findRolesByMenuName("activity_doc"));
            model.addAttribute("perspective_doc", roleService.findRolesByMenuName("perspective_doc"));
            model.addAttribute("statusList", UserStatusENUM.values());
            model.addAttribute("error", ERROR_CREATE_NEW_USER);
            return "user/user-create";
        }
        return "redirect:/general/users";
    }

    @GetMapping("/general/users/userDelete/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public String deleteUser(@PathVariable("id") int id) {
        Users users = usersService.findUserById(id);
        users.getEmployee().setAccount(null);
        usersService.deleteUser(id);
        return "redirect:/general/users";
    }

    @GetMapping("/general/users/userUpdate/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public String updateUserForm(@PathVariable("id") int id,
                                 Model model) {
        Users users = usersService.findUserById(id);
        model.addAttribute("users", users);
        model.addAttribute("employee", roleService.findRolesByMenuName("employee"));
        model.addAttribute("profession", roleService.findRolesByMenuName("profession"));
        model.addAttribute("department", roleService.findRolesByMenuName("department"));
        model.addAttribute("schedule", roleService.findRolesByMenuName("schedule"));
        model.addAttribute("vocation", roleService.findRolesByMenuName("vocation"));
        model.addAttribute("store_personal", roleService.findRolesByMenuName("store_personal"));
        model.addAttribute("store_work", roleService.findRolesByMenuName("store_work"));
        model.addAttribute("store_meas", roleService.findRolesByMenuName("store_meas"));
        model.addAttribute("store_scloth", roleService.findRolesByMenuName("store_scloth"));
        model.addAttribute("activity_work", roleService.findRolesByMenuName("activity_work"));
        model.addAttribute("activity_control", roleService.findRolesByMenuName("activity_control"));
        model.addAttribute("activity_doc", roleService.findRolesByMenuName("activity_doc"));
        model.addAttribute("perspective_doc", roleService.findRolesByMenuName("perspective_doc"));
        if (users.getEmployee().getEmployeeStatusENUM().equals(EmployeeStatusENUM.CLOSED)) {
            model.addAttribute("statusList", Arrays.stream(UserStatusENUM.values())
                    .filter(e -> !e.equals(UserStatusENUM.ON)));
        } else {
            model.addAttribute("statusList", UserStatusENUM.values());
        }
        return "user/user-update";
    }

    @PostMapping("/general/users/userUpdate")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public String updateUser(Users users) {
        Users usersById = usersService.findUserById(users.getUserId());
        users.setUserName(usersById.getUserName());
        users.setEmployee(usersById.getEmployee());
        users.setPassword(usersById.getPassword());
        usersService.updateUser(users);
        return "redirect:/general/users/userUpdate/%d".formatted(users.getUserId());
    }

    @GetMapping("/general/users/recovery/{id}")
    public String sendMessage(@PathVariable("id") int id, Model model) {
        Users users = usersService.findUserById(id);
        String tempPassword = randomPass();
        users.setPassword(tempPassword);
        users.setPassword(passwordEncoder.encode(users.getPassword()));
        if (users.getEmployee().getEmployeeStatusENUM().getTitle().equals("Закрыт")) {
            model.addAttribute("users", usersService.findAllUsers().stream()
                    .sorted(Comparator.comparingInt(Users::getUserId))
                    .toList());
            model.addAttribute("error", "ОШИБКА! У работника " + users.getEmployee() +
                    ", " + users.getEmployee().getProfession().getProfessionName() + " отсутствует (удален) e-mail, " +
                    "и/или работник находится в статусе Закрыт, восстановить учетную запись возможности нет");
            return "user/user-menu";
        }
        try {
            mailSenderService.send(
                    users.getEmployee().getEmail(),
                    "Восстановление данных для входа в систему",
                    String.format("Логин: %s%nПароль: %s%nСтатус учетной записи: " +
                                    "%s%nАдрес: http://@@@/login",
                            users.getUserName(),
                            tempPassword,
                            users.getUserStatusENUM().getTitle()));
            usersService.updateUser(users);
        } catch (Exception e) {
            model.addAttribute("users", usersService.findAllUsers().stream()
                    .sorted(Comparator.comparingInt(Users::getUserId))
                    .toList());
            model.addAttribute("error", "ОШИБКА! У работника " + users.getEmployee() +
                    ", " + users.getEmployee().getProfession().getProfessionName() + " отсутствует (удален) e-mail, " +
                    "и/или работник находится в статусе Закрыт, восстановить учетную запись возможности нет");
            return "user/user-menu";
        }
        return "redirect:/general/users";
    }
}