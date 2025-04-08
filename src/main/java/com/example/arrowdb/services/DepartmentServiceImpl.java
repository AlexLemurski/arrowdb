package com.example.arrowdb.services;

import com.example.arrowdb.entity.Department;
import com.example.arrowdb.repositories.DepartmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentRepository departmentRepository;

    @Override
    @Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED)
    public Department findDepartmentById(Integer id) {
        Department department = null;
        Optional<Department> optional = departmentRepository.findById(id);
        if (optional.isPresent()) {
            department = optional.get();
        }
        return department;
    }

    @Override
    @Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED)
    public List<Department> findAllDepartments() {
        return departmentRepository.findAll().stream()
                .sorted(Comparator.comparingInt(Department::getDepId))
                .toList();
    }

    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void saveDepartment(Department department) {
        departmentRepository.save(department);
    }

    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void deleteDepartment(Integer id) {
        departmentRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED)
    public List<Department> findAllDepartmentsForMainMenu() {
        return departmentRepository.findAllDepartmentsForMainMenu();
    }

    @Override
    @Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED)
    public Department findDepartmentByIdForView(int id) {
        return departmentRepository.findDepartmentByIdForView(id);
    }

    @Override
    @Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED)
    public List<Department> findAllDepartmentsForSchedule() {
        return departmentRepository.findAllDepartmentsForSchedule();
    }

    @Override
    @Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED)
    public List<Department> findAllActiveDepartments() {
        return departmentRepository.findAllActiveDepartments();
    }

}