package com.example.arrowdb.services;

import com.example.arrowdb.entity.Employee;

import java.util.List;

public interface EmployeeService {
    List<Employee> findAllEmployees();
    Employee findEmployeeById(Integer id);
    void saveEmployee(Employee employee);
    void deleteEmployeeById(Integer id);

    List<Employee> findEmployeeForCreateAccount();

    List<Employee> findAllEmployeeForVocations();
    List<Employee> findAllEmployeeForMainMenu();
    Employee findEmployeeByIdForView(int id);

    List<Employee> findAllEmployeesForPersonalInstrument();
    List<Employee> findAllEmployeesForWorkInstrument();
    List<Employee> findAllEmployeesForMeasInstrument();
    List<Employee> findAllEmployeesForSpecialCloth();

    List<Employee> findAllActiveEmployees();

    void changeEmployeeStatusOnVocation();
    void changeEmployeeStatusOnActive();

    List<Employee> findEmployeeMailSend();

}
