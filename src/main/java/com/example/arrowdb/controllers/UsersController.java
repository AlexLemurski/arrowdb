package com.example.arrowdb.controllers;

import com.example.arrowdb.auxiliary.MailSenderService;
import com.example.arrowdb.entity.Users;
import com.example.arrowdb.entity_service.UserServiceEntity;
import com.example.arrowdb.enums.EmployeeStatusENUM;
import com.example.arrowdb.enums.UserStatusENUM;
import com.example.arrowdb.services.EmployeeService;
import com.example.arrowdb.services.RoleService;
import com.example.arrowdb.services.UsersService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

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
    private final SessionRegistry sessionRegistry;
    private final UserServiceEntity userServiceEntity;

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
        userServiceEntity.getRoles(model, roleService);
        model.addAttribute("employeeList", employeeService.findEmployeeForCreateAccount());
        model.addAttribute("statusList", UserStatusENUM.values());
        return "user/user-create";
    }

    @PostMapping("/general/users/userCreate")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public String createUser(@Valid @ModelAttribute Users users,
                             Model model) {
        String login = String.format("%s%s_%s_%s",
                users.getEmployee().getName().substring(0, 1).toUpperCase(),
                users.getEmployee().getMiddleName().substring(0, 1).toUpperCase(),
                users.getEmployee().getSurName(),
                users.getEmployee().getEmpId());
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
                                  "%s%nАдрес: https://@@@/login",
                            users.getUserName(),
                            tempPassword,
                            users.getUserStatusENUM().getTitle()));
        } catch (Exception e) {
            users.setPassword(null);
            users.getEmployee().setAccount(null);
            usersService.deleteUser(users.getUserId());
            userServiceEntity.getRoles(model, roleService);
            model.addAttribute("employeeList", employeeService.findEmployeeForCreateAccount());
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
        userServiceEntity.getRoles(model, roleService);
        model.addAttribute("users", users);
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
        for (Object principal : sessionRegistry.getAllPrincipals()) {
            if (principal instanceof UserDetails && ((UserDetails) principal)
                    .getUsername().equals(usersById.getUserName())) {
                List<SessionInformation> sessions =
                        sessionRegistry.getAllSessions(principal, false);
                for (SessionInformation sessionInfo : sessions) {
                    sessionInfo.expireNow();
                }
            }
        }
        return "redirect:/general/users/userUpdate/%d".formatted(users.getUserId());
    }

    @GetMapping("/general/users/recovery/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
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
                                        ", " + users.getEmployee().getProfession().getProfessionName() +
                                        " отсутствует (удален) e-mail, и/или работник находится в статусе Закрыт, " +
                                        "восстановить учетную запись возможности нет");
            return "user/user-menu";
        }
        try {
            mailSenderService.send(
                    users.getEmployee().getEmail(),
                    "Восстановление данных для входа в систему",
                    String.format("Логин: %s%nПароль: %s%nСтатус учетной записи: " +
                                  "%s%nАдрес: https://@@@/login",
                            users.getUserName(),
                            tempPassword,
                            users.getUserStatusENUM().getTitle()));
            usersService.updateUser(users);
        } catch (Exception e) {
            model.addAttribute("users", usersService.findAllUsers().stream()
                    .sorted(Comparator.comparingInt(Users::getUserId))
                    .toList());
            model.addAttribute("error", "ОШИБКА! У работника " + users.getEmployee() +
                                        ", " + users.getEmployee().getProfession().getProfessionName() +
                                        " отсутствует (удален) e-mail, и/или работник находится в статусе Закрыт, " +
                                        "восстановить учетную запись возможности нет");
            return "user/user-menu";
        }
        return "redirect:/general/users";
    }

    @GetMapping("/general/users/logoutUser/{username}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public String logoutUser(@PathVariable String username) {
        for (Object principal : sessionRegistry.getAllPrincipals()) {
            if (principal instanceof UserDetails && ((UserDetails) principal).getUsername().equals(username)) {
                List<SessionInformation> sessions = sessionRegistry.getAllSessions(principal, false);
                for (SessionInformation sessionInfo : sessions) {
                    sessionInfo.expireNow();
                }
            }
        }
        return "redirect:/general/users";
    }

    @GetMapping("/general/users/logoutAllUser")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public String logoutAllUser() {
        for (Object principal : sessionRegistry.getAllPrincipals()) {
            if (principal instanceof UserDetails && !((UserDetails) principal).getUsername().equals("admin")) {
                List<SessionInformation> sessions = sessionRegistry.getAllSessions(principal, false);
                for (SessionInformation sessionInfo : sessions) {
                    sessionInfo.expireNow();
                }
            }
        }
        return "redirect:/general/users";
    }

    @GetMapping("/general/users/disableAllUser")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public String disableAllUserGet() {
        return "user/user-menu";
    }

    @PostMapping("/general/users/disableAllUser")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public String disableAllUser() {
        List<Users> usersList = usersService.findAllUsers();
        for (Users user : usersList) {
            user.setUserStatusENUM(UserStatusENUM.OFF);
            usersService.updateUser(user);
        }
        return "redirect:/general/users";
    }
}