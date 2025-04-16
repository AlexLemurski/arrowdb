package com.example.arrowdb.services;

import com.example.arrowdb.entity.Employee;
import com.example.arrowdb.repositories.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;

    @Override
    @Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED)
    public List<Employee> findAllEmployees() {
        return employeeRepository.findAll().stream()
                .sorted(Comparator.comparingInt(Employee::getEmpId))
                .toList();
    }

    @Override
    @Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED)
    public Employee findEmployeeById(Integer id) {
        Employee employee = null;
        Optional<Employee> optional = employeeRepository.findById(id);
        if (optional.isPresent()) {
            employee = optional.get();
        }
        return employee;
    }

    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void saveEmployee(Employee employee) {
        employeeRepository.save(employee);
    }

    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void deleteEmployeeById(Integer id) {
        employeeRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED)
    public List<Employee> findEmployeeForCreateAccount() {
        return employeeRepository.findEmployeeForCreateAccount().stream()
                .sorted(Comparator.comparingInt(Employee::getEmpId))
                .toList();
    }

    @Override
    @Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED)
    public List<Employee> findAllEmployeeForVocations() {
        return employeeRepository.findAllEmployeeForVocations();
    }

    @Override
    @Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED)
    public List<Employee> findAllEmployeeForMainMenu() {
        return employeeRepository.findAllEmployeeForMainMenu();
    }

    @Override
    @Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED)
    public Employee findEmployeeByIdForView(int id) {
        return employeeRepository.findEmployeeByIdForView(id);
    }

    @Override
    @Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED)
    public List<Employee> findAllEmployeesForPersonalInstrument() {
        return employeeRepository.findAllEmployeesForPersonalInstrument();
    }

    @Override
    @Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED)
    public List<Employee> findAllEmployeesForWorkInstrument() {
        return employeeRepository.findAllEmployeesForWorkInstrument();
    }

    @Override
    @Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED)
    public List<Employee> findAllEmployeesForMeasInstrument() {
        return employeeRepository.findAllEmployeesForMeasInstrument();
    }

    @Override
    public List<Employee> findAllEmployeesForSpecialCloth() {
        return employeeRepository.findAllEmployeesForSpecialCloth();
    }

    @Override
    @Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED)
    public List<Employee> findAllActiveEmployees() {
        return employeeRepository.findAllActiveEmployees();
    }

    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void changeEmployeeStatusOnVocation() {
        employeeRepository.changeEmployeeStatusOnVocation();
    }

    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void changeEmployeeStatusOnActive() {
        employeeRepository.changeEmployeeStatusOnActive();
    }

    @Override
    @Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED)
    public List<Employee> findEmployeeMailSend() {
        return employeeRepository.findEmployeeMailSend();
    }

    @Override
    @Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED)
    public Employee findEmployeeByUserName(String name) {
        return employeeRepository.findEmployeeByUserName(name);
    }

}