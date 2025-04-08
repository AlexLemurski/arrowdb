package com.example.arrowdb.services;

import com.example.arrowdb.entity.Department;

import java.util.List;

public interface DepartmentService {
    Department findDepartmentById(Integer id);
    List<Department> findAllDepartments();
    void saveDepartment(Department department);
    void deleteDepartment(Integer id);

    List<Department> findAllDepartmentsForMainMenu();
    Department findDepartmentByIdForView(int id);
    List<Department> findAllDepartmentsForSchedule();

    List<Department> findAllActiveDepartments();

}
