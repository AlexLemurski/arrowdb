package com.example.arrowdb.entity_service;

import com.example.arrowdb.auxiliary.Auxiliary;
import com.example.arrowdb.entity.Employee;
import com.example.arrowdb.entity.Users;
import com.example.arrowdb.enums.ConstructionControlStatusENUM;
import com.example.arrowdb.enums.EmployeeStatusENUM;
import com.example.arrowdb.services.EmployeeService;
import com.example.arrowdb.services.UsersService;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

import static com.example.arrowdb.enums.EmployeeStatusENUM.*;
import static com.example.arrowdb.enums.UserStatusENUM.OFF;

@Service
public class EmployeeServiceEntity extends Auxiliary {

    public final String getAccountOfEmployee(@NotNull Employee employee) {
        String empAccount;
        if (employee.getAccount() != null) {
            empAccount = employee.getAccount().getUserName();
        } else {
            empAccount = "";
        }
        return empAccount;
    }

    public final List<Employee> employeeListForMainMenu(EmployeeService employeeService,
                                                  @NotNull UserDetails userDetails) {
        List<Employee> employeeList;
        if (userDetails.getAuthorities().stream()
                    .toList()
                    .contains(new SimpleGrantedAuthority("ROLE_EMPLOYEE_CREATE"))
            ||
            userDetails.getAuthorities().stream()
                    .toList()
                    .contains(new SimpleGrantedAuthority("ROLE_ADMIN"))
        ) {
            employeeList = employeeService.findAllEmployeeForMainMenu();
        } else {
            employeeList = employeeService.findAllEmployeeForMainMenu().stream()
                    .filter(e -> !e.getEmployeeStatusENUM().equals(DRAFT))
                    .toList();
        }
        return employeeList;
    }

    public final List<EmployeeStatusENUM> employeeMainMenuStatus(UserDetails userDetails) {
        List<EmployeeStatusENUM> employeeStatusENUM;
        if (checkCreateOrAdminRole(userDetails)) {
            employeeStatusENUM = Arrays.stream(EmployeeStatusENUM.values()).toList();
        } else {
            employeeStatusENUM = Arrays.stream(EmployeeStatusENUM.values())
                    .filter(e -> !e.equals(DRAFT))
                    .toList();
        }
        return employeeStatusENUM;
    }

    public final List<EmployeeStatusENUM> employeeCreateStatus() {
        return Arrays.stream(EmployeeStatusENUM.values())
                .filter(e -> !e.equals(IN_BUSINESS_TRIP))
                .filter(e -> !e.equals(IN_VOCATION))
                .filter(e -> !e.equals(ON_SICK_LEAVE))
                .filter(e -> !e.equals(CLOSED))
                .toList();
    }

    public final List<EmployeeStatusENUM> employeeUpdateStatus(@NotNull Employee employee) {
        if (employee.getEmployeeStatusENUM().equals(DRAFT)) {
            return Arrays.stream(EmployeeStatusENUM.values())
                    .filter(e -> !e.equals(IN_BUSINESS_TRIP))
                    .filter(e -> !e.equals(IN_VOCATION))
                    .filter(e -> !e.equals(ON_SICK_LEAVE))
                    .filter(e -> !e.equals(CLOSED))
                    .toList();
        } else {
            return Arrays.stream(EmployeeStatusENUM.values())
                    .filter(e -> !e.equals(DRAFT))
                    .filter(e -> !e.equals(IN_VOCATION))
                    .filter(e -> !e.equals(ON_SICK_LEAVE))
                    .filter(e -> !e.equals(IN_BUSINESS_TRIP))
                    .toList();
        }
    }

    public final void offUserStatusAfterCloseEmployee(@NotNull Employee employee, UsersService usersService) {
        if (employee.getEmployeeStatusENUM().equals(EmployeeStatusENUM.CLOSED)) {
            try {
                Users users = usersService.findUsersByUserName(employee.getAccount().getUserName());
                users.setUserStatusENUM(OFF);
            } catch (NullPointerException e) {
                e.getStackTrace();
            }
        }
    }

    public final boolean checkEmployeeRelationshipsBeforeClose(@NotNull Employee employeeById, Employee employee) {
        return !employeeById.getPersonalInstrumentList().isEmpty() &&
               employee.getEmployeeStatusENUM().equals(EmployeeStatusENUM.CLOSED) ||
               !employeeById.getWorkInstrumentList().isEmpty() &&
               employee.getEmployeeStatusENUM().equals(EmployeeStatusENUM.CLOSED) ||
               !employeeById.getMeasInstrumentList().isEmpty() &&
               employee.getEmployeeStatusENUM().equals(EmployeeStatusENUM.CLOSED) ||
               !employeeById.getSpecialClothEmployeeList().isEmpty() &&
               employee.getEmployeeStatusENUM().equals(EmployeeStatusENUM.CLOSED) ||
               !employeeById.getWorkObjectChiefList().isEmpty() &&
               employee.getEmployeeStatusENUM().equals(EmployeeStatusENUM.CLOSED) ||
               !employeeById.getWorkObjectPTOList().isEmpty() &&
               employee.getEmployeeStatusENUM().equals(EmployeeStatusENUM.CLOSED) ||
               !employeeById.getWorkObjectSupplierList().isEmpty() &&
               employee.getEmployeeStatusENUM().equals(EmployeeStatusENUM.CLOSED) ||
               !employeeById.getWorkObjectStoreKeeperList().isEmpty() &&
               employee.getEmployeeStatusENUM().equals(EmployeeStatusENUM.CLOSED) ||
               !employeeById.getResponsibleFromContractorList().stream()
                       .filter(e -> e.getConstructionControlStatusENUM()
                               .equals(ConstructionControlStatusENUM.ACTIVE))
                       .toList().isEmpty() &&
               employee.getEmployeeStatusENUM().equals(EmployeeStatusENUM.CLOSED) ||
               !employeeById.getResponsibleFromSKContractorList().stream()
                       .filter(e -> e.getConstructionControlStatusENUM()
                               .equals(ConstructionControlStatusENUM.ACTIVE))
                       .toList().isEmpty() &&
               employee.getEmployeeStatusENUM().equals(EmployeeStatusENUM.CLOSED);
    }

}