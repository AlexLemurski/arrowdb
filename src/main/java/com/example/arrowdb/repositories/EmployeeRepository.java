package com.example.arrowdb.repositories;

import com.example.arrowdb.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Integer> {

    @Override
    @Query("""
            select e from Employee e
            left join fetch e.account
            left join fetch e.profession
            left join fetch e.department
            where e.empId =:id
            """)
    Optional<Employee> findById(Integer id);

    @Query("""
            select e from Employee e
            where e.account.userName=:name
            """)
    Employee findEmployeeByUserName(String name);

    @Query("""
            select e from Employee e
            where e.employeeStatusENUM != 0 and e.employeeStatusENUM != 5
            order by e.empId
            """)
    List<Employee> findAllActiveEmployees();

    @Query("""
            select e from Employee e
            where e.employeeStatusENUM != 0
            and e.employeeStatusENUM != 5
            and e.account is null
            and e.email is not null
            order by e.empId
            """)
    List<Employee> findEmployeeForCreateAccount();

    @Query(value = """
            select e from Employee e
            where e.email is not null
            order by e.empId
            """)
    List<Employee> findEmployeeMailSend();

    @Query("""
            select e from Employee e
            left join fetch e.profession
            left join fetch e.department
            order by e.empId
            """)
    List<Employee> findAllEmployeeForMainMenu();

    @Query("""
            select e from Employee e
            left join fetch e.profession
            left join fetch e.department
            left join fetch e.vocationList
            where (e.employeeStatusENUM != 0 and e.employeeStatusENUM != 5)
            order by e.empId
            """)
    List<Employee> findAllEmployeeForVocations();

    @Query("""
            select e from Employee e
            left join fetch e.account
            left join fetch e.department
            left join fetch e.profession
            left join fetch e.vocationList
            left join fetch e.personalInstrumentList
            left join fetch e.workInstrumentList
            left join fetch e.measInstrumentList
            left join fetch e.specialClothEmployeeList
            left join fetch e.workObjectChiefList
            left join fetch e.workObjectPTOList
            left join fetch e.workObjectStoreKeeperList
            left join fetch e.workObjectSupplierList
            left join fetch e.responsibleFromContractorList
            left join fetch e.responsibleFromSKContractorList
            left join fetch e.employeeEquipmentList
            left join fetch e.employeeWorkList
            left join fetch e.employeeObserverList
            where e.empId=:id
            """)
    Employee findEmployeeByIdForView(int id);

    @Query("""
            select e from Employee e
            left join fetch e.profession
            left join fetch e.personalInstrumentList
            where e.employeeStatusENUM != 0 and e.employeeStatusENUM != 5
            order by e.empId
            """)
    List<Employee> findAllEmployeesForPersonalInstrument();

    @Query("""
            select e from Employee e
            left join fetch e.profession
            left join fetch e.workInstrumentList
            where e.employeeStatusENUM != 0 and e.employeeStatusENUM != 5
            order by e.empId
            """)
    List<Employee> findAllEmployeesForWorkInstrument();

    @Query("""
            select e from Employee e
            left join fetch e.profession
            left join fetch e.measInstrumentList
            where e.employeeStatusENUM != 0 and e.employeeStatusENUM != 5
            order by e.empId
            """)
    List<Employee> findAllEmployeesForMeasInstrument();

    @Query("""
            select e from Employee e
            left join fetch e.profession
            left join fetch e.specialClothEmployeeList sce
            left join fetch sce.specialCloth
            where e.employeeStatusENUM != 0 and e.employeeStatusENUM != 5
            order by e.empId
            """)
    List<Employee> findAllEmployeesForSpecialCloth();

    @Modifying
    @Query(nativeQuery = true,
            value = """
            update employee set employee_status = 3 from vocation
            where CURRENT_DATE between date_start and date_end
            and employee.emp_id=vocation.employee_id;
            """)
    void changeEmployeeStatusOnVocation();

    @Modifying
    @Query(nativeQuery = true,
            value = """
            update employee set employee_status = 1 from vocation
            where CURRENT_DATE not between date_start and date_end
            and employee.emp_id=vocation.employee_id;
            """)
    void changeEmployeeStatusOnActive();

}