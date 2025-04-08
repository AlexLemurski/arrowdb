package com.example.arrowdb.repositories;

import com.example.arrowdb.history.EmployeeAUD;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface EmployeeAUDRepository extends JpaRepository<EmployeeAUD, Integer> {

    @Query("""
            select e from EmployeeAUD e
            left join fetch e.profession
            left join fetch e.department
            left join fetch e.exampleRevEntity as ex
            where e.empId=:id
            order by ex.id desc
            """)
    List<EmployeeAUD> findAllEmployeeAUDById(Integer id);

}