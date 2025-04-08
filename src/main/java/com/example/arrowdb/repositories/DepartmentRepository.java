package com.example.arrowdb.repositories;

import com.example.arrowdb.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Integer> {

    @Query("""
            select d from Department d
            left join fetch d.departmentChief dc
            left join fetch dc.profession
            left join fetch d.personalOfDepartment pd
            left join fetch pd.profession
            left join fetch d.workInstrumentList
            left join fetch d.measInstrumentList
            left join fetch d.documentDepartmentList
            where d.depId=:id
            """)
    Department findDepartmentByIdForView(int id);

    @Query("""
            select d from Department d
            left join fetch d.departmentChief dc
            left join fetch dc.profession
            left join fetch d.personalOfDepartment pd
            left join fetch pd.profession
            order by d.depId
            """)
    List<Department> findAllDepartmentsForMainMenu();

    @Query("""
            select d from Department d
            left join fetch d.departmentChief dc
            left join fetch dc.profession
            left join fetch d.personalOfDepartment pd
            left join fetch pd.profession
            where d.profAndDepStatusENUM != 1
            and (pd.employeeStatusENUM != 0 or pd is null)
            and (pd.employeeStatusENUM != 5 or pd is null)
            order by d.depId
            """)
    List<Department> findAllDepartmentsForSchedule();

    @Query("""
            select d from Department d
            where d.profAndDepStatusENUM = 0
            order by d.depId
            """)
    List<Department> findAllActiveDepartments();

}